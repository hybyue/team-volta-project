package com.example.volta_lang.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.volta_lang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RequestBook extends AppCompatActivity {


    private Calendar selectedStartDate;
    private ImageView url, backB;
    private TextView nameV, locationV, currentDating, priceses, totalNight, price, totalNights, guestVieww, guestVieww2, guestVieww3, totalPrice;
    private EditText editDate, howlong, howGuests;
    private Button requestB;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    int parent_price;
    int totalDays = 1;
    int totalGuest = 1;
    int totalChild = 0;
    int totalInfant = 0;

    ImageView img;
    ProgressBar progressBar;
    int defaultPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);

        String name = getIntent().getStringExtra("Venue");
        String location = getIntent().getStringExtra("Location");
        String image = getIntent().getStringExtra("Image");
        String priceV = getIntent().getStringExtra("Price");


        editDate = findViewById(R.id.editDate);
        howGuests = findViewById(R.id.how_many_guest);
        howlong = findViewById(R.id.how_long_day);
        totalNight = findViewById(R.id.totalNight);
        totalNights = findViewById(R.id.totalNights);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        guestVieww = findViewById(R.id.gestsView);
        guestVieww2 = findViewById(R.id.gestsView2);
        guestVieww3 = findViewById(R.id.gestsView3);
        totalPrice = findViewById(R.id.totalPrice);
        img = findViewById(R.id.url);
        progressBar = findViewById(R.id.progressBar);
        backB = findViewById(R.id.backB);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);



        if (totalGuest <= 1) {
            guestVieww.setText(totalGuest + " Guest");
        } else if (totalChild <= 1) {
            guestVieww.setText(totalChild + " Child");
        } else if (totalInfant <= 1) {
            guestVieww.setText(totalInfant + " infant");
        }

        if (totalDays <= 1) {
            totalNight.setText("only one day");
        }

        totalNights.setText(" x " + totalDays + " night");

        howGuests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGuests();
            }
        });


        howlong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });


        Calendar c = Calendar.getInstance();
        String currentDates = DateFormat.getDateInstance(DateFormat.LONG).format(c.getTime());
        currentDating = findViewById(R.id.viewDate);

        currentDating.setText(currentDates);

        url = findViewById(R.id.url);
        nameV = findViewById(R.id.venueName1);
        locationV = findViewById(R.id.locate);
        priceses = findViewById(R.id.prices);
        price = findViewById(R.id.priceless);


        nameV.setText(name);
        locationV.setText(location);
        Glide.with(this)
                .load(image)
                .into(url);
        price.setText(priceV);
        priceses.setText(priceV);


        defaultPrice = Integer.parseInt(price.getText().toString().replace(",", ""));
        parent_price = defaultPrice + 2000;
        totalPrice.setText("Php " + numberFormat.format(parent_price));



        requestB = findViewById(R.id.requestBook);
        requestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBooking();
            }
        });


    }

    private void requestBooking() {
        fStore.collection("BookOfUser")
                .document(fAuth.getCurrentUser().getUid())
                .collection("CurrentUser")
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                Toast.makeText(RequestBook.this, "You already have an existing booking", Toast.LENGTH_SHORT).show();
                            } else {
                                performNewBooking();
                            }
                        } else {
                            Log.e("TAG", "Error checking existing booking: " + task.getException().getMessage());
                            Toast.makeText(RequestBook.this, "Error checking existing booking", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void performNewBooking() {
        String currentsDate;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat saveFormat = new SimpleDateFormat("MMMM dd, yyyy");
        currentsDate = saveFormat.format(calendar.getTime());

        final HashMap<String, Object> book = new HashMap<>();

        progressBar.setVisibility(View.VISIBLE);

        fStore.collection("users").document(fAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String username = document.getString("name");

                                Log.d("TAG", "Username: " + username);

                                String imageUrl = getIntent().getStringExtra("Image");

                                book.put("username", username);
                                book.put("name", nameV.getText().toString());
                                book.put("price", price.getText().toString());
                                book.put("currentDate", currentsDate);
                                book.put("dateSet", currentDating.getText().toString());
                                book.put("days", totalNight.getText().toString());
                                book.put("totalGuest", guestVieww.getText().toString());
                                book.put("totalPrice", priceses.getText().toString());
                                book.put("imageUrl", imageUrl);

                                fStore.collection("BookOfUser")
                                        .document(fAuth.getCurrentUser().getUid())
                                        .collection("CurrentUser")
                                        .add(book)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(RequestBook.this, "Book successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                finish();
                                            }
                                        });
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(RequestBook.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.e("TAG", "Error fetching user data: " + task.getException().getMessage());
                            Toast.makeText(RequestBook.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void showGuests() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.how_many_guests);

        ImageView exit1 = dialog.findViewById(R.id.exit1);
        Button savePeople = dialog.findViewById(R.id.savePeople);
        EditText clearGuest = dialog.findViewById(R.id.clearGuest);

        Button addGuest = dialog.findViewById(R.id.addguest);
        Button minusGuest = dialog.findViewById(R.id.minusguest);
        EditText guest = dialog.findViewById(R.id.adultTotal);
        guest.setHighlightColor(Color.parseColor("#FFFFFF"));

        Button addChild = dialog.findViewById(R.id.addChild);
        Button minusChild = dialog.findViewById(R.id.minusChild);
        EditText child = dialog.findViewById(R.id.childTotal);


        Button addInfant = dialog.findViewById(R.id.addInfant);
        Button minusInfant = dialog.findViewById(R.id.minusInfant);
        EditText infant = dialog.findViewById(R.id.infantTotal);


        guest.setText(String.valueOf(totalGuest));
        child.setText(String.valueOf(totalChild));
        infant.setText(String.valueOf(totalInfant));


        guest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Update totalGuest variable when the user types in the EditText
                try {
                    totalGuest = Integer.parseInt(editable.toString());
                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid integer
                    totalGuest = 1; // or set it to a default value
                }
            }
        });

        addGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalGuest < 20) {
                    totalGuest++;
                } else {
                    Toast.makeText(getApplicationContext(), "Reach the maximum number", Toast.LENGTH_SHORT).show();
                }
                guest.setText(String.valueOf(totalGuest));
            }
        });

        minusGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalGuest > 1) {
                    totalGuest--;
                }
                guest.setText(String.valueOf(totalGuest));
            }
        });

        child.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Update totalGuest variable when the user types in the EditText
                try {
                    totalChild = Integer.parseInt(editable.toString());
                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid integer
                    totalChild = 1; // or set it to a default value
                }
            }
        });

        addChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalChild < 20) {
                    totalChild++;
                } else {
                    Toast.makeText(getApplicationContext(), "Reach the maximum number", Toast.LENGTH_SHORT).show();
                }
                child.setText(String.valueOf(totalChild));
            }
        });

        minusChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalChild > 0) {
                    totalChild--;
                }
                child.setText(String.valueOf(totalChild));
            }
        });

        infant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    totalInfant = Integer.parseInt(editable.toString());
                } catch (NumberFormatException e) {
                    totalInfant = 1;
                }
            }
        });

        addInfant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalInfant < 10) {
                    totalInfant++;
                } else {
                    Toast.makeText(getApplicationContext(), "Reach the maximum number", Toast.LENGTH_SHORT).show();
                }
                infant.setText(String.valueOf(totalInfant));
            }
        });

        minusInfant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalInfant > 0) {
                    totalInfant--;
                }
                infant.setText(String.valueOf(totalInfant));
            }
        });


        exit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        savePeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalGuest == 1) {
                    guestVieww.setText(totalGuest + " guest");
                } else if (totalGuest >= 2) {
                    guestVieww.setText(totalGuest + " guests");

                }

                if (totalChild == 1) {
                    guestVieww2.setText(", " + totalChild + " child");
                } else if (totalChild >= 2) {
                    guestVieww2.setText(", " + totalChild + " childs");

                }

                if (totalInfant == 1) {
                    guestVieww3.setText(", " + totalInfant + " infant");
                } else if (totalInfant >= 2) {
                    guestVieww3.setText(", " + totalInfant + " infants");

                }

                guest.setEnabled(false);
                child.setEnabled(false);
                infant.setEnabled(false);

                dialog.dismiss();


            }
        });


        clearGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalGuest = 1;
                totalChild = 0;
                totalInfant = 0;

                guest.setText(String.valueOf(totalGuest));
                child.setText(String.valueOf(totalChild));
                infant.setText(String.valueOf(totalInfant));

            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_total_days);


        ImageView exits = dialog.findViewById(R.id.exit1);
        EditText clear = dialog.findViewById(R.id.clearGuest);
        Button save = dialog.findViewById(R.id.savePeople);
        Button minus = dialog.findViewById(R.id.minusDay);
        Button add = dialog.findViewById(R.id.addDay);
        TextView hows = dialog.findViewById(R.id.howMany);

        // Store the default price

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        totalPrice.setText("Php " + numberFormat.format(parent_price));


        // Set the initial value
        hows.setText(String.valueOf(totalDays));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalDays < 5) {
                    totalDays++;
                } else {
                    Toast.makeText(getApplicationContext(), "Reach the maximum dates", Toast.LENGTH_SHORT).show();
                }
                hows.setText(String.valueOf(totalDays));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalDays > 1) {
                    totalDays--;
                }
                hows.setText(String.valueOf(totalDays));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStartDate == null) {
                    Toast.makeText(getApplicationContext(), "Please select a date first.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int total_day = totalDays;
                int total_price = total_day * defaultPrice;

                 parent_price = total_price + 2000;



                if (totalDays == 1) {
                    totalNight.setText(total_day + " night");
                    totalNights.setText(" x " + totalDays + " night");
                    totalPrice.setText("Php " + numberFormat.format(parent_price));
                } else {
                    totalNight.setText(total_day + " nights");
                    totalNights.setText(" x " + totalDays + " nights");
                    totalPrice.setText("Php " + numberFormat.format(parent_price));
                }

                Calendar endDate = (Calendar) selectedStartDate.clone();
                endDate.add(Calendar.DAY_OF_MONTH, totalDays - 1);

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                SimpleDateFormat selectDay = new SimpleDateFormat("MMMM d", Locale.US);

                String formattedStartDate = sdf.format(selectedStartDate.getTime());
                String formattedEndDate = selectDay.format(endDate.getTime());

                currentDating.setText(formattedStartDate + " to " + formattedEndDate);

                String formattedTotalPrice = NumberFormat.getNumberInstance().format(total_price);
                priceses.setText(formattedTotalPrice);
                hows.setEnabled(false);

                dialog.dismiss();
            }
        });

        exits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalDays = 1;
                hows.setText(String.valueOf(totalDays));
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }


    private void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Create a Calendar instance and set it to the selected date
                selectedStartDate = Calendar.getInstance();
                selectedStartDate.set(year, month, day);

                // Get the current date
                Calendar currentDate = Calendar.getInstance();

                // Calculate the date 3 days from now
                Calendar futureDate = Calendar.getInstance();
                futureDate.add(Calendar.DAY_OF_MONTH, 3);

                if (selectedStartDate.before(currentDate) || selectedStartDate.before(futureDate)) {
                    // Date is invalid, show an error message or handle it as needed
                    // For example, you can show a Toast message
                    Toast.makeText(getApplicationContext(), "Please select a date within the next 3 days.", Toast.LENGTH_SHORT).show();
                } else {
                    // Format the selected date to be user-friendly
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                    String formattedDate = sdf.format(selectedStartDate.getTime());

                    // Showing the formatted date in the textView
                    currentDating.setText(formattedDate);
                }
            }
        }, 2023, 01, 20);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Disable past dates
        datePickerDialog.show();
    }
}

