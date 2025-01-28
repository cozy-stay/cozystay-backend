package rw.cozy.cozybackend.utils;


import rw.cozy.cozybackend.exceptions.BadRequestException;

public interface Constants {

    String DEFAULT_PAGE_NUMBER = "0";



    String DEFAULT_PAGE_SIZE = "100";



    int MAX_PAGE_SIZE = 1000;


    static void validatePageNumberAndSize(int pageNumber, int pageSize) {
        if (pageNumber < 1) {
            throw new BadRequestException("Page number is less than zero.");
        }

        if (pageSize > Constants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size is greater than " + Constants.MAX_PAGE_SIZE);
        }
    }
}
