package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Order;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.OrderDetail;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IOrderDetailRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private IOrderDetailRepository orderDetailRepository;
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("posteventblooms@gmail.com");

        mailSender.send(message);
        System.out.println("Email sent to " + to);
    }
    /// gửi mail khi đã đặt hàng
    public void sendOrderConfirmationEmail(Order order) {
        String to = order.getUser().getEmail();
        String subject = "Xác nhận đơn hàng #" + order.getOrderID();

        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedTotalPrice = format.format(order.getTotalPrice()) + " VNĐ";

        // Lấy danh sách OrderDetail dựa trên orderID
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder_OrderID(order.getOrderID());

        // Tạo nội dung chi tiết đơn hàng
        StringBuilder orderDetailsHtml = new StringBuilder();
        orderDetailsHtml.append("<h2>Xin chào ").append(order.getUser().getUsername()).append(",</h2>")
                .append("<p>Cảm ơn bạn đã đặt hàng tại cửa hàng của chúng tôi!</p>")
                .append("<p>Thông tin đơn hàng của bạn:</p>")
                .append("<ul>")
                .append("<li>Mã đơn hàng: ").append(order.getOrderID()).append("</li>")
                .append("<li>Ngày đặt hàng: ").append(order.getOrderDate()).append("</li>")
                .append("<li>Tổng tiền: ").append(formattedTotalPrice).append("</li>")
                .append("<li>Địa chỉ giao hàng: ").append(order.getShippingAddress()).append("</li>")
                .append("</ul>")
                .append("<h3>Chi tiết đơn hàng:</h3>")
                .append("<table style='border-collapse: collapse; width: 100%;'>")
                .append("<tr>")
                .append("<th style='border: 1px solid black; padding: 8px;'>Tên hoa</th>")
                .append("<th style='border: 1px solid black; padding: 8px;'>Số lượng</th>")
                .append("<th style='border: 1px solid black; padding: 8px;'>Giá</th>")
                .append("</tr>");

        // Kiểm tra danh sách OrderDetail có rỗng không
        if (orderDetails.isEmpty()) {
            orderDetailsHtml.append("<tr><td colspan='3' style='border: 1px solid black; padding: 8px;'>Không có chi tiết đơn hàng.</td></tr>");
        } else {
            // Duyệt qua từng OrderDetail để thêm vào email
            for (OrderDetail detail : orderDetails) {
                String formattedPrice = format.format(detail.getPrice()) + " VNĐ";
                orderDetailsHtml.append("<tr>")
                        .append("<td style='border: 1px solid black; padding: 8px;'>")
                        .append(detail.getFlowerBatch().getFlowerName())
                        .append("</td>")
                        .append("<td style='border: 1px solid black; padding: 8px; text-align: center;'>") // Căn giữa
                        .append(detail.getQuantity())
                        .append("</td>")
                        .append("<td style='border: 1px solid black; padding: 8px; text-align: right;'>") // Căn phải
                        .append(formattedPrice)
                        .append("</td>")
                        .append("</tr>");

            }
        }

        orderDetailsHtml.append("</table>")
                .append("<p>Chúng tôi sẽ liên hệ với bạn trong thời gian sớm nhất để giao hàng.</p>")
                .append("<p>Trân trọng,</p>")
                .append("<p>Đội ngũ Flower Exchange</p>");

        String body = orderDetailsHtml.toString();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // `true` để gửi nội dung HTML

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
