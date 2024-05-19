package pizzashop.repository;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PaymentRepository {
    private String filename;
    private List<Payment> paymentList;

    public PaymentRepository(String filename){
        this.paymentList = new ArrayList<>();
        this.filename = filename;
        readPayments();
    }

    private void readPayments(){
        //ClassLoader classLoader = PaymentRepository.class.getClassLoader();
        File file = new File(filename);
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = null;
            while((line=br.readLine())!=null){
                Payment payment=getPayment(line);
                paymentList.add(payment);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Payment getPayment(String line){
        Payment item=null;
        if (line==null|| line.equals("")) return null;
        StringTokenizer st=new StringTokenizer(line, ",");
        int tableNumber= Integer.parseInt(st.nextToken());
        String type= st.nextToken();
        double amount = Double.parseDouble(st.nextToken());
        LocalDateTime orderDate = LocalDateTime.parse(st.nextToken());
        item = new Payment(tableNumber, PaymentType.valueOf(type), amount, orderDate);
        return item;
    }

    public void add(int table, PaymentType type, double amount, LocalDateTime dateTime){
        Payment payment= new Payment(table, type, amount, dateTime);
        paymentList.add(payment);
        writeOne(payment);
    }

    public List<Payment> getAll(){
        return paymentList;
    }

    public void writeOne(Payment payment){
        //ClassLoader classLoader = PaymentRepository.class.getClassLoader();
        System.out.println("Platesc!");
        File file = new File(filename);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            System.out.println(payment.toString());
            bw.write(payment.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}