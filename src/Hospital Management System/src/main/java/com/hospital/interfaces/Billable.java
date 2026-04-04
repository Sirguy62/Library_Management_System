package com.hospital.interfaces;

import com.hospital.model.Bill;
import java.util.List;

public interface Billable {

    double calculateBill(double baseAmount);

    double applyDiscount(double amount, double discountPercent);

    List<Bill> getBills();

    double getTotalOutstanding();
}