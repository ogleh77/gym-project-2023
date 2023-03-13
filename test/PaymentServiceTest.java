import com.example.gymdesktop2023.dto.PaymentService;
import com.example.gymdesktop2023.entity.Box;
import com.example.gymdesktop2023.entity.main.PaymentBuilder;
import com.example.gymdesktop2023.entity.main.Payments;
import com.example.gymdesktop2023.models.PaymentModel;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class PaymentServiceTest {

    private final PaymentModel model = new PaymentModel();

    @Test
    void insertPayment() throws SQLException {
        Payments payment = new PaymentBuilder()
                .setCustomerFK("4303923")
                .setExpDate(LocalDate.now().plusDays(20))
                .setDiscount(1)
                .setPaidBy("Zaad Service")
                .build();

        payment.setBox(new Box(1, null, true));
        System.out.println(payment);
        model.insertPayment(payment.getCustomerFK(), "Male", payment);
    }

    @Test
    void pendPayment() throws SQLException {
        Payments payment = new PaymentBuilder()
                .setPaymentID(1)
                .setExpDate(LocalDate.now().plusDays(25))
                .build();
        payment.setBox(new Box(1, null, true));

        PaymentService.holdPayment(payment, 2);

        System.out.println("Hold...");

    }

    @Test
    void unPendPayment() throws SQLException {
        Payments payment = new PaymentBuilder()
                .setPaymentID(1)
                .build();


        PaymentService.unHoldPayment(payment);

        System.out.println("Un hold...");

    }



}