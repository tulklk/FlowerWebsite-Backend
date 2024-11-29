package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.UserCreationRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.request.UserUpdateRequest;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.UserResponse;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.enums.Role;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.exception.AppException;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.exception.ErrorCode;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IEventFlowerPostingRepository;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.INotificationsRepository;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IUserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    IUserRepository userRepository;
    INotificationsRepository notificationsRepository;
    IEventFlowerPostingRepository iEventFlowerPostingRepository;
    PasswordEncoder passwordEncoder;

    private JavaMailSender mailSender;

    // USER METHODS
    @Override
    public User findById(int userId) {
        return userRepository.findById(userId).orElse(null); // Return the user or null if not found
    }

    @Override
    public User createUser(UserCreationRequest request) {
        // Kiểm tra email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Thiết lập vai trò mặc định là BUYER
        Set<String> roles = new HashSet<>();
        roles.add(Role.BUYER.name());
        user.setRoles(roles);

        // Tạo mã xác nhận
        user.generateVerificationToken();

        // Lưu đối tượng User vào cơ sở dữ liệu
        User savedUser = userRepository.save(user);

        // Gửi email xác nhận
        sendVerificationEmail(user.getEmail(), user.getVerificationToken());

        return savedUser;
    }

    @Override
//    @PostAuthorize("returnObject.email == authentication.principal.getClaim('email')")
    public UserResponse getUser(int userID) {
        UserResponse userResponse = new UserResponse();

        // Retrieve User entity
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Convert User to UserResponse
        userResponse.setUserID(user.getUserID());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setAddress(user.getAddress());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRoles(user.getRoles());
        userResponse.setStatus(user.getAvailableStatus());
        userResponse.setCreatedAt(user.getCreatedAt());

        return userResponse;
    }

    @Override
//    @PostAuthorize("returnObject.email == authentication.principal.email")
    public User updateUser(int userID, UserUpdateRequest request) {
        log.info("Attempting to update user with ID: {}", userID);
        log.info("Request data: {}", request);
        User user = userRepository.findById(userID)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", userID);
                    return new AppException(ErrorCode.USER_NOT_EXISTED);
                });
        log.info("Current user details before update: {}", user);

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        // Save the updated user
        User updatedUser = userRepository.save(user);
        // Log the updated user details
        log.info("User updated successfully: {}", updatedUser);
        return updatedUser;
    }

    @Override
//    @PostAuthorize("returnObject.email == authentication.principal.email")
    public User UpdateUserIntoSeller(int userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // Update User into Seller
        Set<String> roles = user.getRoles();
        roles.add(Role.SELLER.name());
        user.setRoles(roles);
        // Save the updated user

        User updatedUser = userRepository.save(user);
        log.info("User updated into seller successfully: {}", updatedUser);
        return updatedUser;
    }

    // ADMIN METHODS
    // Get all users
    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return userRepository.findAll();
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(int userID) {
        try {
            // Delete all notifications and related entities before deleting the user
            iEventFlowerPostingRepository.deleteByUser_userID(userID);
            notificationsRepository.deleteByUser_userID(userID);
            userRepository.deleteById(userID);
        } catch (Exception e) {
            log.error("Error occurred while deleting user with ID {}: {}", userID, e.getMessage());
            throw new AppException(ErrorCode.DELETE_USER_ERROR, e);  // Pass the original exception
        }
    }

    public User setStatus(int userID, String status) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setAvailableStatus(status); // Cập nhật trạng thái người dùng

        return userRepository.save(user); // Lưu và trả về đối tượng User đã cập nhật
    }



    ////////////////////////////////////////////////////////////////////////////////
    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        int password = random.nextInt(999999); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("%06d", password); // Đảm bảo mật khẩu có 6 chữ số
    }
    public String resetPassword(String email) {
        // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu không
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            return "Tài khoản không tồn tại!";
        }
        User user = userOptional.get();
        // Tạo mật khẩu ngẫu nhiên (6 số)
        String newPassword=generateRandomPassword();
        // Mã hóa mật khẩu mới và cập nhật vào cơ sở dữ liệu
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // Gửi email với mật khẩu mới cho người dùng
        sendEmailResetPassword(email, newPassword);
        return "Mật khẩu đã được gửi đến email của bạn!";

    }
    private void sendEmailResetPassword(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mật khẩu mới của bạn");
        message.setText("Mật khẩu mới của bạn là: " + newPassword);
        message.setFrom("posteventblooms@gmail.com");

        mailSender.send(message);
        System.out.println("Đã gửi email đến " + to);
    }
    // Hàm gửi email xác nhận
    private void sendVerificationEmail(String to, String verificationToken) {
        String verificationLink = "http://localhost:8080/identity/users/verify-email?token=" + verificationToken;
        //gửi email với định dạng (HTML, hình ảnh, tệp đính kèm)
        MimeMessage message = mailSender.createMimeMessage();
        // Nội dung email dưới dạng HTML
        String body = "<div style=\"font-family: Arial, sans-serif; background-color: #f3f4f6; padding: 20px;\">" +
                "<div style=\"max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);\">" +
                "<h2 style=\"color: #333333; text-align: center;\">Xin chào!</h2>" +
                "<p style=\"color: #555555; font-size: 16px; line-height: 1.6;\">" +
                "Cảm ơn bạn đã quan tâm đến sản phẩm hoa của chúng tôi! Để hoàn tất đăng ký, vui lòng nhấn vào liên kết dưới đây để xác nhận email của bạn:</p>" +
                "<div style=\"text-align: center; margin: 20px 0;\">" +
                "<a href=\"" + verificationLink + "\" style=\"display: inline-block; font-size: 16px; color: #ffffff; background-color: #FFC0CB; padding: 12px 24px; text-decoration: none; border-radius: 6px;\">" +
                "Xác nhận email của tôi</a>" +
                "</div>" +
                "<p style=\"color: #555555; font-size: 16px; line-height: 1.6;\">Cảm ơn bạn đã tin tưởng lựa chọn dịch vụ của chúng tôi. Chúng tôi hy vọng có thể giúp bạn tạo nên những khoảnh khắc đẹp với hoa.</p>" +
                "<p style=\"text-align: center; color: #777777; font-size: 14px;\">&copy; 2024 Website Bán Hoa Sự Kiện. All rights reserved.</p>" +
                "</div>" +
                "</div>";
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart, false nếu gửi mỗi text
            helper.setTo(to);
            helper.setSubject("Xác nhận email của bạn");
            helper.setText(body, true); // true = send as HTML
            message.setFrom("posteventblooms@gmail.com");

            mailSender.send(message);
            System.out.println("Email xác nhận đã được gửi tới " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Gửi email thất bại: " + e.getMessage());
        }
    }
    //verifyemail
    public String verifyEmail(String token) {
        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isEmpty()) {
            return "Token không hợp lệ hoặc đã hết hạn!";
        }

        User user = userOptional.get();
        user.setVerificationToken(null); // Xóa mã xác nhận sau khi xác thực
        user.setEmailVerified(true);
        user.setAvailableStatus("available"); // Đặt trạng thái là 'available'
        userRepository.save(user);

        return "Bạn đã xác nhận thành công";
    }
    public User setStatusemail() {
        User user = userRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true); // Cập nhật trạng thái admin

        return userRepository.save(user);
    }
    public  User setStatusUser(int  userID){
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setAvailableStatus("available");
        user.setVerificationToken(null);
        user.setEmailVerified(true);
        return userRepository.save(user);
    }


}
