package com.example.petapp.modals;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.petapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PetAppointmentPicker extends DialogFragment {

    public interface AppointmentCallback {
        void onAppointmentSelected(Long timestamp, String petType);
        void onDismiss();
    }

    // Pet types array
    private static final String[] PET_TYPES = {
            "Dog", "Cat", "Bird", "Rabbit",
            "Hamster", "Guinea Pig", "Reptile",
            "Fish", "Other"
    };

    public static class PetAppointmentPickerDialog extends DialogFragment {
        private Long selectedDateTimestamp;
        private String selectedPetType;
        private AppointmentCallback callback;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Create bottom sheet dialog
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());

            // Inflate custom layout
            View view = LayoutInflater.from(getContext()).inflate(
                    R.layout.dialog_pet_appointment_picker, null
            );

            // Date picker button
            MaterialButton datePickerButton = view.findViewById(R.id.date_picker_button);
            MaterialButton timePickerButton = view.findViewById(R.id.time_picker_button);
            MaterialButton confirmButton = view.findViewById(R.id.confirm_button);
            TextInputLayout petTypeDropdown = view.findViewById(R.id.pet_type_dropdown);
            AutoCompleteTextView petTypeAutoComplete = view.findViewById(R.id.pet_type_autocomplete);

            // Setup pet type dropdown
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    PET_TYPES
            );
            petTypeAutoComplete.setAdapter(adapter);

            // Date picker
            datePickerButton.setOnClickListener(v -> {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                        .datePicker()
                        .setTitleText("Select Appointment Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    selectedDateTimestamp = selection;
                    datePickerButton.setText(new SimpleDateFormat(
                            "MMM dd, yyyy",
                            Locale.getDefault()
                    ).format(new Date(selection)));
                });

                datePicker.show(getParentFragmentManager(), "DATE_PICKER");
            });

            // Time picker
            timePickerButton.setOnClickListener(v -> {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setTitleText("Select Appointment Time")
                        .build();

                timePicker.addOnPositiveButtonClickListener(selection -> {
                    // Combine selected date and time
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(selectedDateTimestamp != null
                            ? selectedDateTimestamp
                            : System.currentTimeMillis());

                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());

                    selectedDateTimestamp = calendar.getTimeInMillis();

                    timePickerButton.setText(String.format(
                            "%02d:%02d %s",
                            timePicker.getHour() % 12 == 0 ? 12 : timePicker.getHour() % 12,
                            timePicker.getMinute(),
                            timePicker.getHour() >= 12 ? "PM" : "AM"
                    ));
                });

                timePicker.show(getParentFragmentManager(), "TIME_PICKER");
            });

            // Confirm button
            confirmButton.setOnClickListener(v -> {
                selectedPetType = petTypeAutoComplete.getText().toString();

                if (selectedDateTimestamp != null && !selectedPetType.isEmpty()) {
                    if (callback != null) {
                        callback.onAppointmentSelected(selectedDateTimestamp, selectedPetType);
                    }
                    dismiss();
                }
            });

            bottomSheetDialog.setContentView(view);
            return bottomSheetDialog;
        }

        public void setCallback(AppointmentCallback callback) {
            this.callback = callback;
        }
    }
}