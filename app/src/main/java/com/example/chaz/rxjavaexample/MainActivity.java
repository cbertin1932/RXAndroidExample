package com.example.chaz.rxjavaexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private Button mRXButton;
    private TextView mRXTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mRXButton = (Button) findViewById(R.id.rxButton);;
        mRXTextView = (TextView) findViewById(R.id.rxTextView);
        Observable<String> mButtonStream = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emi) throws Exception {
                mRXButton.setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent me)
                    {
                        if(me.getAction() == MotionEvent.ACTION_DOWN)
                            emi.onNext("Pressed");
                        else if(me.getAction() == MotionEvent.ACTION_UP)
                            emi.onNext("Released");
                        return false;
                    }
                });

                emi.setCancellable(new Cancellable()
                {
                    @Override
                    public  void cancel() throws Exception
                    {
                        mRXButton.setOnClickListener(null);
                    }
                });

            }
        });

        mButtonStream.observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        Toast.makeText(MainActivity.this,"OnTouched",Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Consumer<String>()
                {
                    @Override
                    public void accept (String newText)
                    {
                        mRXTextView.setText(newText);
                    }
                });
    }
}
