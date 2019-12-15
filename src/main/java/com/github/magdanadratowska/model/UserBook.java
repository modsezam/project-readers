package com.github.magdanadratowska.model;

import java.time.LocalDateTime;

public class UserBook {
    private Book book;
    private LocalDateTime additionDate;
    private int rate;
    private boolean isActive;
    private boolean isOwned;
    private String review;

    public UserBook() {
    }

    public UserBook(Book book, LocalDateTime additionDate, int rate) {
        this.book = book;
        this.additionDate = additionDate;
        this.rate = rate;
        this.isActive =true;
    }

    public UserBook(Book book, boolean isActive, boolean isOwned) {
        this.book = book;
        this.isActive = isActive;
        this.isOwned = isOwned;
    }

    public UserBook(Book book, LocalDateTime additionDate, int rate, boolean isActive, String review) {
        this.book = book;
        this.additionDate = additionDate;
        this.rate = rate;
        this.isActive = isActive;
        this.review = review;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(LocalDateTime additionDate) {
        this.additionDate = additionDate;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public boolean getIsOwned() {
        return isOwned;
    }

    public void setIsOwned(boolean owned) {
        isOwned = owned;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
