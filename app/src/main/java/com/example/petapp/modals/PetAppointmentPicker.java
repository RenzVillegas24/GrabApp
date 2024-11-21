package com.example.petapp.modals;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.petapp.R;
import com.example.petapp.database.UserDatabaseHelper;
import com.example.petapp.models.User;
import com.example.petapp.utils.UserSessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PetAppointmentPicker extends DialogFragment {

    public interface AppointmentCallback {
        void onAppointmentSelected(Long timestamp, String petType, String additionalNotes, String address);
        void onDismiss();
    }

    // Expanded pet types array
    private static final String[] PET_TYPES = {
            "Dog", "Cat", "Bird", "Rabbit",
            "Hamster", "Guinea Pig", "Reptile",
            "Fish", "Horse", "Ferret",
            "Hedgehog", "Other"
    };

    public static class PetAppointmentPickerDialog extends DialogFragment {
        private Long selectedDateTimestamp;
        private String selectedPetType;
        private String selectedTime;
        private String selectedAddress;
        private AppointmentCallback callback;

        private UserSessionManager userSessionManager;


        // Input fields
        private TextInputEditText dateInput;
        private TextInputEditText timeInput;
        private AutoCompleteTextView petTypeAutoComplete;
        private TextInputEditText notesInput;
        private TextInputEditText addressInput;
        private MaterialButton confirmButton;

        // Text input layout
        private TextInputLayout dateInputLayout;
        private TextInputLayout timeInputLayout;
        private TextInputLayout petTypeInputLayout;
        private TextInputLayout addressInputLayout;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create bottom sheet dialog
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());


            // Inflate custom layout
            View view = getLayoutInflater().inflate(
                    R.layout.dialog_pet_appointment_picker, null
            );

            // Initialize views
            dateInput = view.findViewById(R.id.date_input);
            timeInput = view.findViewById(R.id.time_input);
            petTypeAutoComplete = view.findViewById(R.id.pet_type_autocomplete);
            notesInput = view.findViewById(R.id.notes_input);
            addressInput = view.findViewById(R.id.address_input);
            confirmButton = view.findViewById(R.id.confirm_button);

            // Initialize text input layouts
            dateInputLayout = view.findViewById(R.id.date_input_layout);
            timeInputLayout = view.findViewById(R.id.time_input_layout);
            petTypeInputLayout = view.findViewById(R.id.pet_type_dropdown);
            addressInputLayout = view.findViewById(R.id.address_input_layout);

            userSessionManager = new UserSessionManager(requireContext());

            // Get the current user
            User user = userSessionManager.getCurrentUser();

            addressInput.setText(user.getAddress());
            selectedAddress = user.getAddress();

            // Setup pet type dropdown
            ArrayAdapter<String> petTypeAdapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    PET_TYPES
            );
            petTypeAutoComplete.setAdapter(petTypeAdapter);

            // Date picker
            dateInput.setOnClickListener(v -> {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                        .datePicker()
                        .setSelection(selectedDateTimestamp != null ? selectedDateTimestamp : MaterialDatePicker.todayInUtcMilliseconds())
                        .setTitleText("Select Appointment Date")
                        .setCalendarConstraints((new CalendarConstraints.Builder())
                                .setValidator(new CalendarConstraints.DateValidator() {
                                    @Override
                                    public int describeContents() {
                                        return 0;
                                    }

                                    @Override
                                    public void writeToParcel(@NonNull Parcel dest, int flags) {
                                    }

                                    @Override
                                    public boolean isValid(long date) {
                                        return date >= MaterialDatePicker.todayInUtcMilliseconds();
                                    }
                                })
                                .build())
                        .build();

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    selectedDateTimestamp = selection;
                    dateInput.setText(new SimpleDateFormat(
                            "MMM dd, yyyy",
                            Locale.getDefault()
                    ).format(new Date(selection)));
                    validateForm();
                });

                datePicker.show(getParentFragmentManager(), "DATE_PICKER");
            });

            // Time picker
            MaterialTimePicker.Builder timePicker = new MaterialTimePicker.Builder();
            timeInput.setOnClickListener(v -> {
                String[] timeParts = selectedTime != null ? selectedTime.split("[: ]") : null;

                if (timeParts != null) {
                    int hour = Integer.parseInt(timeParts[0]);
                    int minute = Integer.parseInt(timeParts[1]);
                    boolean isPM = timeParts[2].equals("PM");

                    if (isPM && hour != 12) {
                        hour += 12;
                    } else if (!isPM && hour == 12) {
                        hour = 0;
                    }

                    timePicker
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setTitleText("Select Appointment Time")
                            .setHour(hour)
                            .setMinute(minute);
                } else {
                    timePicker
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setTitleText("Select Appointment Time");
                }

                MaterialTimePicker timePickerBuilt = timePicker.build();

                timePickerBuilt.addOnPositiveButtonClickListener(selection -> {
                    selectedTime = String.format(
                            Locale.getDefault(),
                            "%02d:%02d %s",
                            timePickerBuilt.getHour() % 12 == 0 ? 12 : timePickerBuilt.getHour() % 12,
                            timePickerBuilt.getMinute(),
                            timePickerBuilt.getHour() >= 12 ? "PM" : "AM"
                    );

                    timeInput.setText(selectedTime);
                    validateForm();
                });

                timePickerBuilt.show(getParentFragmentManager(), "TIME_PICKER");
            });

            // Pet type selection listener
            petTypeAutoComplete.setOnItemClickListener((parent, view1, position, id) -> {
                selectedPetType = PET_TYPES[position];
                validateForm();
            });

            // Add text watchers for validation
            notesInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    validateForm();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            addressInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    selectedAddress = s.toString().trim();
                    validateForm();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Confirm button
            confirmButton.setOnClickListener(v -> {
                if (isFormValid()) {
                    // Combine selected date and time
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(selectedDateTimestamp);

                    // Parse time and set hours/minutes
                    String[] timeParts = selectedTime.split("[: ]");
                    int hour = Integer.parseInt(timeParts[0]);
                    int minute = Integer.parseInt(timeParts[1]);
                    boolean isPM = timeParts[2].equals("PM");

                    if (isPM && hour != 12) {
                        hour += 12;
                    } else if (!isPM && hour == 12) {
                        hour = 0;
                    }

                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);

                    // Get notes
                    String notes = notesInput.getText().toString().trim();

                    // Callback with appointment details
                    if (callback != null) {
                        callback.onAppointmentSelected(
                                calendar.getTimeInMillis(),
                                selectedPetType,
                                notes,
                                selectedAddress
                        );
                    }
                    dismiss();
                }
            });

            // Initial form validation
            confirmButton.setEnabled(false);

            bottomSheetDialog.setContentView(view);
            return bottomSheetDialog;
        }

        // Form validation method
        private void validateForm() {
            confirmButton.setEnabled(isFormValid());
        }

        // Check if all required fields are filled
        private boolean isFormValid() {
            boolean isValid = true;

            // check if time is not lower than current time
            if (selectedDateTimestamp != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selectedDateTimestamp);
                // add the selected time to the selected date
                if (selectedTime != null) {
                    String[] timeParts = selectedTime.split("[: ]");
                    int hour = Integer.parseInt(timeParts[0]);
                    int minute = Integer.parseInt(timeParts[1]);
                    boolean isPM = timeParts[2].equals("PM");

                    if (isPM && hour != 12) {
                        hour += 12;
                    } else if (!isPM && hour == 12) {
                        hour = 0;
                    }

                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                }

                if (calendar.before(Calendar.getInstance())) {
                    timeInputLayout.setErrorEnabled(true);
                    timeInputLayout.setError("Selected time cannot be in the past");
                    isValid = false;
                } else {
                    if (selectedTime == null) {
                        timeInputLayout.setErrorEnabled(true);
                        timeInputLayout.setError("Time cannot be empty");
                        isValid = false;
                    } else {
                        timeInputLayout.setError(null);
                        timeInputLayout.setErrorEnabled(false);
                    }
                }
                dateInputLayout.setError(null);
                dateInputLayout.setErrorEnabled(false);
            } else {
                dateInputLayout.setErrorEnabled(true);
                dateInputLayout.setError("Date cannot be empty");
                isValid = false;
            }

            if (selectedPetType == null || selectedPetType.isEmpty()) {
                petTypeInputLayout.setErrorEnabled(true);
                petTypeInputLayout.setError("Pet type cannot be empty");
                isValid = false;
            } else {
                petTypeInputLayout.setError(null);
                petTypeInputLayout.setErrorEnabled(false);
            }

            if (selectedAddress == null || selectedAddress.isEmpty()) {
                addressInputLayout.setErrorEnabled(true);
                addressInputLayout.setError("Address cannot be empty");
                isValid = false;
            } else {
                addressInputLayout.setError(null);
                addressInputLayout.setErrorEnabled(false);
            }

            return isValid;
        }

        public void setCallback(AppointmentCallback callback) {
            this.callback = callback;
        }
    }
}