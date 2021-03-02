//package springbatch.first_project.mocks;
//import static java.lang.System.out;
//import java.sql.*;
//
//import org.jooq.tools.jdbc.MockConnection;
//import org.jooq.tools.jdbc.MockDataProvider;
//import org.jooq.tools.jdbc.MockFileDatabase;
//
//public class Mocking {
//    public static void main(String[] args) throws Exception {
//        MockDataProvider db = new MockFileDatabase(
//            Mocking.class.getResourceAsStream("classpath:person.sql"));
// 
////        try (Connection c = new MockConnection(db);
////            Statement s = c.createStatement()) {
//// 
////            out.println("Actors:");
////            out.println("-------");
////            try (ResultSet rs = s.executeQuery(
////                "select first_name, last_name from actor")) {
////                while (rs.next())
////                    out.println(rs.getString(1) 
////                        + " " + rs.getString(2));
////            }
//// 
////            out.println();
////            out.println("Actors and their films:");
////            out.println("-----------------------");
////            try (ResultSet rs = s.executeQuery(
////                "select first_name, last_name, count(*)\n" +
////                "from actor\n" +
////                "join film_actor using (actor_id)\n" +
////                "group by actor_id, first_name, last_name\n" +
////                "order by count(*) desc")) {
////                while (rs.next())
////                    out.println(rs.getString(1) 
////                        + " " + rs.getString(2) 
////                        + " (" + rs.getInt(3) + ")");
////            }
////        }
//    }
//}