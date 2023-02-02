package com.alperez.samples.rxcardverificationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.alperez.samples.rxcardverificationsample.domain.CardType;
import com.alperez.samples.rxcardverificationsample.domain.DomainUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private EditText etCardNum, etExpire, etCVC;
    private Button btnSubmit, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCardNum = findViewById(R.id.edt_card_number);
        etExpire = findViewById(R.id.edt_expires);
        etCVC = findViewById(R.id.edt_cvc);
        btnSubmit = findViewById(R.id.btn_submit);
        btnClear = findViewById(R.id.btn_clear);
        btnClear.setEnabled(false);
        btnClear.setOnClickListener(v -> {
            etCardNum.getText().clear();
            etExpire.getText().clear();
            etCVC.getText().clear();
        });

        buildValidationRx();
    }

    private final CompositeDisposable allDisposable = new CompositeDisposable();

    @SuppressLint("CheckResult")
    private void buildValidationRx() {
        final Observable<String> cardNumObservable = RxTextView.textChanges(etCardNum)
                .map(AppUtils::charSequence2String);
        final Observable<String> expiresObservable = RxTextView.textChanges(etExpire)
                .map(AppUtils::charSequence2String);
        final Observable<String> cvcObservable = RxTextView.textChanges(etCVC)
                .map(AppUtils::charSequence2String);


        final Observable<Boolean> btnClearEnabledObservable = ObservableMerge.or(
                cardNumObservable.map(s -> !s.isEmpty()),
                expiresObservable.map(s -> !s.isEmpty()),
                cvcObservable.map(s -> !s.isEmpty())
            );



        final Observable<Boolean> isExpirationValid = expiresObservable.map(DomainUtils::validateExpirationDate);

        final Observable<CardType> cardTypeObservable = cardNumObservable.map(CardType::fromString);

        final Observable<Boolean> isCardTypeValidObservable = cardTypeObservable.map(type -> type != CardType.UNKNOWN);

        final Observable<Boolean> isCheckSumValidObservable = cardNumObservable
                .map(AppUtils::convertCardNumberTextToDigits)
                .map(DomainUtils::validateCardNumberChecksum);

        final Observable<Boolean> isCardNumValidObservable = ObservableMerge.and(
                isCardTypeValidObservable,
                isCheckSumValidObservable);

        final Observable<Boolean> isCvcValid = ObservableMerge.and(
                ObservableMerge.eq(
                        cvcObservable.map(String::length),
                        cardTypeObservable.map(CardType::getCvcLength)
                ),
                cvcObservable.map(AppUtils::checkStringIsNumber)
        );

        final Observable<Boolean> isFormValidObservable = ObservableMerge.and(
                isCardNumValidObservable, isCvcValid, isExpirationValid
        );

        Disposable dispBtnClear = btnClearEnabledObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(btnClear::setEnabled);
        allDisposable.add(dispBtnClear);

        Disposable dispFormValid = isFormValidObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(btnSubmit::setEnabled);
        allDisposable.add(dispFormValid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!allDisposable.isDisposed()) {
            allDisposable.dispose();
        }
    }
}













