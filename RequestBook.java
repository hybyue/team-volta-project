package com.example.volta_lang.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private TextView nameV, locationV, currentDating, totalNight, price, guestVieww, guestVieww2, guestVieww3, totalPrice;
    private EditText editDate, howlong, howGuests;
    private Button requestB;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    int parent_price;
    int totalGuest = 1;
    int totalChild = 0;
    int totalInfant = 0;

    ImageView img;
    ProgressBar progressBar;
    int defaultPrice;
    RadioGroup radioGroup;
    RadioButton radioButton;
    private int selectedRadioButtonId = -1;


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
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        guestVieww = findViewById(R.id.gestsView);
        guestVieww2 = findViewById(R.id.gestsView2);
        guestVieww3 = findViewById(R.id.gestsView3);
        totalPrice = findViewById(R.id.totalPrice);
        img = findViewById(R.id.url);
        progressBar = findViewById(R.id.progressBar);
        backB = findViewById(R.id.backB);


        totalNight.setText("Morning 7:30 am");

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        c.add(Calendar.DAY_OF_MONTH, 2);
        String currentDates = DateFormat.getDateInstance(DateFormat.LONG).format(c.getTime());
        currentDating = findViewById(R.id.viewDate);

        currentDating.setText(currentDates);

        url = findViewById(R.id.url);
        nameV = findViewById(R.id.venueName1);
        locationV = findViewById(R.id.locate);

        price = findViewById(R.id.priceless);


        nameV.setText(name);
        locationV.setText(location);
        Glide.with(this)
                .load(image)
                .into(url);
        price.setText(priceV);


        defaultPrice = Integer.parseInt(price.getText().toString().replace(",", ""));
        parent_price = defaultPrice + 2000;
        totalPrice.setText("Php " + numberFormat.format(parent_price));


        requestB = findViewById(R.id.requestBook);
        requestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(RequestBook.this).inflate(R.layout.alert_dialog, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(RequestBook.this);
                alert.setView(view);

                Button yes = view.findViewById(R.id.yesButton);
                Button no = view.findViewById(R.id.noButton);
                TextView title = view.findViewById(R.id.logoutText);
                TextView descrip = view.findViewById(R.id.textDescrip);

                yes.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                yes.setTextColor(Color.BLACK);

                no.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                no.setTextColor(Color.BLACK);

                title.setText("Warning!");

                descrip.setText("Are you sure about your the set up?");

                AlertDialog alertDialog = alert.create();
                alertDialog.setCancelable(true);


                // Set click listener for the "Yes" button
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // User clicked "Yes," log out
                        requestBooking();

                        alertDialog.dismiss();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                alertDialog.show();


            }
        });


    }

    private void requestBooking() {
        fStore.collection("BookOfUser")
                .whereEqualTo("userUid", fAuth.getCurrentUser().getUid())
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
        String guest = guestVieww.getText().toString().trim();


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
                                String gmail = document.getString("email");

                                String imageUrl = getIntent().getStringExtra("Image");

                                book.put("userUid", fAuth.getCurrentUser().getUid());
                                book.put("username", username);
                                book.put("userGmail", gmail);
                                book.put("name", nameV.getText().toString());
                                book.put("currentDate", currentsDate);
                                book.put("dateSet", currentDating.getText().toString());
                                book.put("time", totalNight.getText().toString());
                                book.put("totalGuest", guestVieww.getText().toString());
                                book.put("totalPrice", totalPrice.getText().toString());
                                book.put("imageUrl", imageUrl);

                                fStore.collection("BookOfUser")
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
        dialog.setContentView(R.layout.activity_time);


        ImageView exits = dialog.findViewById(R.id.exit1);
        radioGroup = dialog.findViewById(R.id.groupButton);


        if (selectedRadioButtonId != -1) {
            radioGroup.check(selectedRadioButtonId);
        }

        Button saveMoNa = dialog.findViewById(R.id.saveMo);

        saveMoNa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();

                radioButton = radioGroup.findViewById(radioId);

                if (radioButton != null) {
                    if (radioButton.getId() == R.id.morningSet) {
                        totalNight.setText("Morning 7:30 am");
                    } else if (radioButton.getId() == R.id.nightSet) {
                        totalNight.setText("Evening 7:30 pm");
                    }
                }

                // Save the selected radio button ID
                selectedRadioButtonId = radioId;

                dialog.dismiss();
            }
        });


        exits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
                selectedStartDate = Calendar.getInstance();
                selectedStartDate.set(year, month, day);


                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                String formattedDate = sdf.format(selectedStartDate.getTime());
                currentDating.setText(formattedDate);
            }
        }, 2023, 01, 20);

        // Set the minimum date to disable past dates
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, 2); // Set minimum date to the day after the current date
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());

        // Set the maximum date to allow selection within the next 9 months
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.MONTH, 9);
        datePickerDialog.getDatePicker().setMaxDate(futureDate.getTimeInMillis());

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

}