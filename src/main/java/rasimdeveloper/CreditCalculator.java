package rasimdeveloper;
import java.sql.*;
public class CreditCalculator {
    private static Connection connection;

    private static double amountCredit;  // Сумма кредита
    private static double rate;       // Процентная ставка
    private static int countMonths;

    public static void main(String[]args){
        getConnection(); // подключаемся к базе данных
        amountCredit=setAmountCredit(); // получаем Сумму кредита из базы
        rate=setRate();                // получаем Процентную ставку
        countMonths=setCountMonths();  // получаем количество месяцев
       System.out.println("Cумма кредита: "+amountCredit+" руб.");   //////
       System.out.println("Ставка: "+rate+"%");                      ///// выводим на экран
       System.out.println("Срок: "+countMonths+" месяцев.");         /////
        double month_rate=rate/100/12;    // вычисляем месячный процент
        double anuitentPay= amountCredit*((month_rate*Math.pow((1+month_rate),countMonths))/(Math.pow((1+month_rate),countMonths)-1)); // формула
                                                                                                                                     // аннуитетного платежа
        for (int i=1;i<=countMonths;i++){          ////////// создаём цикл от 1 до кол-ва месяцев
            double procentCredit=amountCredit*(rate/100)/countMonths;       /////// Вычисляем долг по процентам
            double basicCredit= anuitentPay-procentCredit;                  ////// Вычисляем основной долг
            amountCredit=amountCredit-anuitentPay+procentCredit;        ////// вычисляем остаток долга
            System.out.println(i+"|"+anuitentPay+"|"+basicCredit+"|"+procentCredit+"|"+amountCredit);   ////// выводим на экран
        }

        try {
            connection.close();
            System.out.println("База закрыта");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static int setCountMonths(){
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet=statement.executeQuery("select time_months from requestions where idrequestion=1"))
        {
            resultSet.next();
            return resultSet.getInt("time_months");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static double setRate(){
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet=statement.executeQuery("select amount_bet from requestions where idrequestion=1"))
        {
            resultSet.next();
           return resultSet.getDouble("amount_bet");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static double setAmountCredit()  {

        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select amount_credit from requestions where idrequestion=1"))
        {
            resultSet.next();
            return  resultSet.getDouble("amount_credit");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private static void getConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/caseplatform","root","rasl_1998");
            System.out.println("База подключена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
