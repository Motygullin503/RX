package ru.ksu.motygullin.rx;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observables.MathObservable;

public class MainActivity extends AppCompatActivity {

    Button btn6;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    TextView textView;

    public static Observable<Integer> task1(@NonNull List<String> list) {
        Observable<Integer> obs = Observable.from(list)
                .map(String::toLowerCase)
                .filter(value -> value.contains("r"))
                .flatMap(value -> Observable.just(value.length()));
        return obs;
    }

    public static Observable<String> task2(@NonNull Observable<String> observable) {
        Observable<String> obs = observable
                .takeUntil(value -> value.equals("END"))
                .filter(value -> !value.equals("END"))
                .distinct();
        return obs;
    }

    public static Observable<Integer> task3(@NonNull Observable<Integer> observable) {
        return MathObservable.sumInteger(observable);
    }

    public static Observable<Integer> task4(@NonNull Observable<Boolean> flagObservable,
                                            @NonNull Observable<Integer> first, @NonNull Observable<Integer> second) {
        Observable<Integer> obs = flagObservable
                .switchMap(bool -> {
                    if (bool) {
                        return first.flatMap(value -> {
                            if (value > 99) {
                                return Observable.error(new Exception());
                            } else {
                                return Observable.just(value);
                            }
                        });
                    } else {
                        return second.flatMap(value -> {
                            if (value > 99) {
                                return Observable.error(new Exception());
                            } else {
                                return Observable.just(value);
                            }
                        });
                    }
                })
                .switchIfEmpty(Observable.error(new Exception()));;
        return obs;
    }

    public static Observable<Integer> task5(@NonNull Observable<Integer> first,
                                            @NonNull Observable<Integer> second) {
        Observable<Integer> obs = Observable.zip(first, second, (value1, value2) -> BigInteger.valueOf(value1)
                .gcd(BigInteger.valueOf(value2)).intValue());
        return obs;
    }

    public static Observable<BigInteger> task6() {
        Observable<BigInteger> obs = Observable.range(1, 100000)
                .map(value -> value * 2)
                .skip(40000)
                .skipLast(40000)
                .filter(value -> value % 3 == 0)
                .cache()
                .reduce(BigInteger.ONE, ((bigInteger, bigInteger2) -> bigInteger.multiply(BigInteger.valueOf(bigInteger2))));
        return obs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        btn5 = (Button) findViewById(R.id.button5);
        btn6 = (Button) findViewById(R.id.button6);

        textView = (TextView)findViewById(R.id.tv);

        btn1.setOnClickListener(view -> {
            List<String> list = new ArrayList<>();
            list.add("Vasya");
            list.add("Dima");
            list.add("Artur");
            list.add("Petya");
            list.add("Roma");
            textView.setText("");
            final String[] result = new String[1];

            task1(list).subscribe(value -> {
                if (textView.getText().equals("")) {
                    result[0] = String.valueOf(value);
                    textView.setText(result[0]);
                } else {
                    result[0] = textView.getText() + ", " + String.valueOf(value);
                    textView.setText(result[0]);
                }
            });

        });
        btn2.setOnClickListener(view -> {
            Observable<String> observable = Observable.just("One", "Two", "Three", "END");
            textView.setText("");
            final String[] result = new String[1];

            task2(observable).subscribe(value ->{
                if (textView.getText().equals("")) {
                    result[0] = String.valueOf(value);
                    textView.setText(result[0]);
                } else {
                    result[0] = textView.getText() + ", " + String.valueOf(value);
                    textView.setText(result[0]);
                }
            });

        });
        btn3.setOnClickListener(view -> {
            Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
            task3(observable).subscribe(value ->{
                textView.setText(String.valueOf(value));
            });
        });
        btn4.setOnClickListener(view -> {
            Observable<Boolean> obsFlag = Observable.just(false);
            Observable<Integer> obs1 = Observable.just(5, 19, 12);
            Observable<Integer> obs2 = Observable.just(9, 90, 99);

            textView.setText("");
            final String[] result = new String[1];

            task4(obsFlag, obs1, obs2).subscribe(value ->{
                if (textView.getText().equals("")) {
                    result[0] = String.valueOf(value);
                    textView.setText(result[0]);
                } else {
                    result[0] = textView.getText() + ", " + String.valueOf(value);
                    textView.setText(result[0]);
                }
            });

        });
        btn5.setOnClickListener(view -> {
            Observable<Integer> obs1 = Observable.just(100, 17, 63);
            Observable<Integer> obs2 = Observable.just(15, 89, 27);

            textView.setText("");
            final String[] result = new String[1];

            task5(obs1, obs2).subscribe(value -> {
                if (textView.getText().equals("")) {
                    result[0] = String.valueOf(value);
                    textView.setText(result[0]);
                } else {
                    result[0] = textView.getText() + ", " + String.valueOf(value);
                    textView.setText(result[0]);
                }
            });

        });
        btn6.setOnClickListener(view -> {
            task6().subscribe(value ->{
                textView.setText(String.valueOf(value));
            });
        });

    }
}


