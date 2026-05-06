package com.example.masroofy;

/**
 * Handles PIN-based authentication logic including validation,
 * verification, and PIN change operations.
 */
public class AUTH {

    /**
     * Verifies whether the provided input matches the stored PIN.
     *
     * @param oldpin the stored PIN to compare against
     * @param input  the user-provided input to verify
     * @return {@code true} if the input matches the stored PIN; {@code false} otherwise
     */
    public boolean verfiypin(String oldpin, String input) {
        return oldpin.equals(input);
    }

    /**
     * Validates that a PIN consists of exactly four numeric digits.
     *
     * @param pin the PIN string to validate
     * @return {@code true} if the PIN is exactly 4 digits; {@code false} otherwise
     */
    public boolean isValidpin(String pin) {
        return pin.matches("\\d{4}");
    }

    /**
     * Validates a PIN change request by verifying the old PIN and ensuring
     * the new PIN meets the required format.
     *
     * @param oldPin the currently stored PIN
     * @param input  the user-provided current PIN for verification
     * @param newPin the new PIN the user wishes to set
     * @return {@code true} if the old PIN is verified and the new PIN is valid; {@code false} otherwise
     */
    public boolean changepin(String oldPin, String input, String newPin) {
        if (verfiypin(oldPin, input) && isValidpin(newPin)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether the provided input ID matches the stored user ID.
     *
     * @param inputid the ID entered by the user
     * @param userid  the actual user ID to compare against
     * @return {@code true} if the IDs match; {@code false} otherwise
     */
    public boolean isValidID(int inputid, int userid) {
        return inputid == userid;
    }
}