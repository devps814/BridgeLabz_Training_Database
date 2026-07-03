package bookstore_app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class BookStoreApp {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        while(true){
            System.out.println("\n===== BOOK STORE =====");
            System.out.println("1. View Catalog");
            System.out.println("2. Add New Book");
            System.out.println("3. Update Book Price");
            System.out.println("4. Purchase Book");
            System.out.println("5. View Audit History");
            System.out.println("6. Exit");

            int choice = Integer.parseInt(scanner.nextLine());
            switch(choice){
                case 1:
                    viewCatalog();
                    break;

                case 2:
                    addNewBook();
                    break;

                case 3:
                    updateBookPrice();
                    break;

                case 4:
                    purchaseBook();
                    break;

                case 5:
                    viewAuditHistory();
                    break;

                case 6:
                    return;
            }
        }
    }

    private static void viewCatalog() {

        String sql =
                "SELECT * FROM books";

        try(
                Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ){

            while(rs.next()){
                System.out.println(rs.getInt("id")
                                + " | "
                                + rs.getString("title")
                                + " | "
                                + rs.getDouble("price")
                );
            }

        }catch(Exception e){

            e.printStackTrace();
        }
    }
    private static void addNewBook() {

        System.out.print("Title : ");
        String title = scanner.nextLine();

        System.out.print("Author : ");
        String author = scanner.nextLine();

        System.out.print("Price : ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Stock : ");
        int stock = Integer.parseInt(scanner.nextLine());

        String sql =
                """
                INSERT INTO books
                (title,author,price,stock)
                VALUES(?,?,?,?)
                """;

        try(
                Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ){

            pstmt.setString(1,title);
            pstmt.setString(2,author);
            pstmt.setDouble(3,price);
            pstmt.setInt(4,stock);

            pstmt.executeUpdate();

            System.out.println("Book Added");

        }catch(Exception e){

            e.printStackTrace();
        }
    }
    private static void updateBookPrice() {

        System.out.print("Book ID : ");

        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("New Price : ");

        double price = Double.parseDouble(scanner.nextLine());

        String sql =
                """
                UPDATE books
                SET price=?
                WHERE id=?
                """;

        try(
                Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ){

            pstmt.setDouble(1,price);
            pstmt.setInt(2,id);

            pstmt.executeUpdate();

            System.out.println("Price Updated");

        }catch(Exception e){

            e.printStackTrace();
        }
    }
    private static void purchaseBook() {

        Connection conn = null;

        try {

            System.out.print("User ID : ");
            int userId = Integer.parseInt(scanner.nextLine());

            System.out.print("Book ID : ");
            int bookId = Integer.parseInt(scanner.nextLine());

            System.out.print("Quantity : ");
            int quantity = Integer.parseInt(scanner.nextLine());

            conn = DBUtil.getConnection();

            conn.setAutoCommit(false);

            // Book Check

            PreparedStatement bookStmt = conn.prepareStatement(
                            """
                            SELECT price,stock
                            FROM books
                            WHERE id=?
                            """
                    );

            bookStmt.setInt(1,bookId);

            ResultSet bookRs = bookStmt.executeQuery();

            if(!bookRs.next()) {
                System.out.println("Book Not Found");

                conn.rollback();
                return;
            }

            double price = bookRs.getDouble("price");
            int stock = bookRs.getInt("stock");

            if(stock < quantity){

                System.out.println("Insufficient Stock");

                conn.rollback();
                return;
            }

            double totalAmount = price * quantity;

            // User Balance Check

            PreparedStatement userStmt = conn.prepareStatement(
                            """
                            SELECT balance
                            FROM users
                            WHERE id=?
                            """
                    );

            userStmt.setInt(1,userId);

            ResultSet userRs = userStmt.executeQuery();

            if(!userRs.next()) {

                System.out.println("User Not Found");

                conn.rollback();
                return;
            }

            double balance = userRs.getDouble("balance");

            if(balance < totalAmount){

                System.out.println("Insufficient Balance");

                conn.rollback();
                return;
            }

            // Update Stock

            PreparedStatement stockUpdate = conn.prepareStatement(
                            """
                            UPDATE books
                            SET stock=stock-?
                            WHERE id=?
                            """
                    );

            stockUpdate.setInt(1, quantity);

            stockUpdate.setInt(2, bookId);

            stockUpdate.executeUpdate();

            // Deduct Balance

            PreparedStatement balanceUpdate = conn.prepareStatement(
                            """
                            UPDATE users
                            SET balance=balance-?
                            WHERE id=?
                            """
                    );

            balanceUpdate.setDouble(1, totalAmount);

            balanceUpdate.setInt(2, userId);

            balanceUpdate.executeUpdate();

            // Create Order

            PreparedStatement orderStmt = conn.prepareStatement(
                            """
                            INSERT INTO orders
                            (user_id,book_id,quantity)
                            VALUES(?,?,?)
                            """
                    );

            orderStmt.setInt(1,userId);
            orderStmt.setInt(2,bookId);
            orderStmt.setInt(3,quantity);

            orderStmt.executeUpdate();

            conn.commit();

            System.out.println("Purchase Successful");

        }
        catch (Exception e){

            try {
                if(conn != null){
                    conn.rollback();
                }

            }catch (Exception ex){

                ex.printStackTrace();
            }

            e.printStackTrace();
        }
    }private static void viewAuditHistory() {

        String sql =
                """
                SELECT *
                FROM price_audit
                ORDER BY changed_at DESC
                """;

        try(
                Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ){

            while(rs.next()) {

                System.out.println(rs.getInt("book_id")
                                + " | "
                                + rs.getDouble(
                                "old_price")
                                + " -> "
                                + rs.getDouble(
                                "new_price")
                                + " | "
                                + rs.getTimestamp(
                                "changed_at")
                );
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }

}
