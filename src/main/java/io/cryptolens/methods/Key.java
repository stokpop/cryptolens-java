package io.cryptolens.methods;

import io.cryptolens.internal.BasicResult;
import io.cryptolens.internal.HelperMethods;
import io.cryptolens.models.ActivateModel;
import io.cryptolens.internal.ActivateResult;
import io.cryptolens.models.DeactivateModel;
import io.cryptolens.models.LicenseKey;

import java.util.HashMap;
import java.util.Map;

/**
 * A collection of methods that operate on a license key. Please see a complete
 * list here: https://app.cryptolens.io/docs/api/v3/Key
 */
public class Key {

    /**
     * Calls the Activate method (https://app.cryptolens.io/docs/api/v3/Activate).
     * @param token The access token with 'Activate' permission.
     * @param RSAPubKey Your RSA Public Key, which can be found at https://app.cryptolens.io/docs/api/v3/QuickStart.
     * @param model Method parameters.
     * @return A LicenseKey object if success and null otherwise.
     */
    public static LicenseKey Activate (String token, String RSAPubKey, ActivateModel model) {

        Map<String,String> extraParams = new HashMap<>();

        // force sign and the new protocol (only in activate)
        extraParams.put("Sign", "true");
        extraParams.put("SignMethod", "1");
        extraParams.put("token", token);

        ActivateResult result = HelperMethods.SendRequestToWebAPI("key/activate", model, extraParams, ActivateResult.class);

        if(result.result == 1) {
            System.err.println("The server returned an error: " + result.message);
            return null;
        }

        return LicenseKey.LoadFromString(RSAPubKey, result.RawResponse);
    }

    /**
     * This method will 'undo' a key activation with a certain machine code.
     * The key should not be blocked, since otherwise this method will throw an error.
     * More info: https://app.cryptolens.io/docs/api/v3/Deactivate
     * @param token The access token with 'Deactivate' permission.
     * @param model Method parameters.
     * @return True if deactivation succeeded and false otherwise.
     */
    public static boolean Deactivate (String token, DeactivateModel model) {

        Map<String,String> extraParams = new HashMap<>();
        extraParams.put("token", token);

        BasicResult result = HelperMethods.SendRequestToWebAPI("key/deactivate", model, extraParams, BasicResult.class);

        if(result.result == 1) {
            System.err.println("The server returned an error: " + result.message);
            return false;
        }

        return true;
    }

}