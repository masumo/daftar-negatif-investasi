package mo.masu.daftarnegatifinvestasi.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import mo.masu.daftarnegatifinvestasi.R;

/**
 * Created by WilayahDua on 23/08/2016.
 */
public class FilteringDialog extends DialogFragment {
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    private TextView txtSector;
    private String sectorValue;
    private String stockValue;
    private Boolean forASEAN;
    private Spinner spinSector;
    private Spinner spinStock;
    private CheckBox cbASEAN;

    public interface FilteringDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    FilteringDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the FilteringDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the FilteringDialogListener so we can send events to the host
            mListener = (FilteringDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement FilteringDialogListener");
        }
    }

    public String getSectorOption (){
        //TextView txtSector = (TextView) getView().findViewById(R.id.textOptSector);
        return stockValue;
    }
    public String getASEANstatus (){
        //TextView txtSector = (TextView) getView().findViewById(R.id.textOptSector);
        String tmp="BUKAN";
        if(forASEAN) tmp="ASEAN";
        return tmp;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.filtering_options,null);
        builder.setView(view);
        spinSector = (Spinner) view.findViewById(R.id.spinnerOptSector);
        spinStock = (Spinner) view.findViewById(R.id.spinnerOptStock);
        cbASEAN = (CheckBox) view.findViewById(R.id.checkBoxASEAN);

        ArrayAdapter<CharSequence> stockAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_stockChoice, android.R.layout.simple_spinner_item);
        stockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinStock.setAdapter(stockAdapter);

        ArrayAdapter<CharSequence> sectorAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_sectorName, android.R.layout.simple_spinner_item);
        sectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSector.setAdapter(sectorAdapter);


        builder.setMessage("Filter Hasil Pencarian")
                .setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                       // spinSector.getSelectedItem().toString();
                        sectorValue= spinSector.getSelectedItem().toString();
                        stockValue= spinStock.getSelectedItem().toString();
                        forASEAN = cbASEAN.isChecked();
                        //fsgfs
                        mListener.onDialogPositiveClick(FilteringDialog.this);
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(FilteringDialog.this);
                    }
                });
        return builder.create();
    }

}
