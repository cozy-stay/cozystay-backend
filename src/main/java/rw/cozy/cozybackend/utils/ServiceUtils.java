package rw.cozy.cozybackend.utils;


import rw.cozy.cozybackend.enums.EVisibility;
import rw.cozy.cozybackend.model.User;

public class ServiceUtils {
    // method to check if a user is valid or deleted
    public static boolean isUserDeleted(User user) {
        return user.getVisibility().equals(EVisibility.VOIDED);
    }
}
