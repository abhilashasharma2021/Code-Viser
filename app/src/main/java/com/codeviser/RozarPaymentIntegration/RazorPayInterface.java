package com.codeviser.RozarPaymentIntegration;

import android.content.Context;

public interface RazorPayInterface {

    void startPayment(Context context,String amount,String currency,String AppName);
}
