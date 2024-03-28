public class UserSession {
    private static String userName; // For simplicity, using static variables. Consider a more secure and scalable approach for a real application.

    public static void setLoggedInUser(String name) {
        userName = name;
    }

    public static String getLoggedInUserName() {
        return userName;
    }
}
