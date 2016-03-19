package com.fakap.blurt.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.fakap.blurt.Constants;
import com.fakap.blurt.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FRIEND_ID = "param1";

    // TODO: Rename and change types of parameters
    private String friendId;

    private OnFragmentInteractionListener mListener;
    private View view;

    public ChatFragment() {
        // Required empty public constructor
    }

    private String authorId;
    private String receiverId;
    private EditText authorEditText;
    private EditText receiverEditText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param friendId Parameter 1.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String friendId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setUpConversation(friendId);
        args.putString(ARG_FRIEND_ID, friendId);
        fragment.setArguments(args);
        return fragment;
    }

    public void setUpConversation(String friendId) {
        if (friendId != null) {
            hideHelp();
        }
        receiverId = friendId;
        authorId = AccessToken.getCurrentAccessToken().getUserId();
    }

    public void setUpConversation() {
        if (receiverId == null) {
            return;
        }
        Constants.firebaseReference.child(authorId).child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String message = (String) dataSnapshot.getValue();
                
                receiverEditText.setText(message);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        authorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Constants.firebaseReference.child(receiverId).child(authorId).setValue(s.toString());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            friendId = getArguments().getString(ARG_FRIEND_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        // set feathered background
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        //LinearLayout chatLayout = (LinearLayout) view.findViewById(R.id.chat_linear_layout);
        //Bitmap bmp = BitmapFactory.decodeResource(getResources(),
        //        R.drawable.feathers);
        //BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bmp);
        //    bitmapDrawable.setAlpha(20);
        //bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        //    chatLayout.setBackground(bitmapDrawable);
        //}

        authorEditText = (EditText) view.findViewById(R.id.my_bubble_edit_text);
        receiverEditText = (EditText) view.findViewById(R.id.friend_bubble_edit_text);

        setUpConversation();

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Gidole-Regular.ttf");
        authorEditText.setTypeface(typeface);
        receiverEditText.setTypeface(typeface);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Force keyboard on
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                showSoftInput(getView(), InputMethodManager.SHOW_IMPLICIT);


        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void hideHelp() {
        ImageView helpView = (ImageView) view.findViewById(R.id.help_image_view);
        helpView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
        //        hideSoftInputFromWindow(_pay_box_helper.getWindowToken(), 0);
        mListener = null;
    }

    public void clearBoxes() {
        authorEditText.setText("");
        receiverEditText.setText("");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
