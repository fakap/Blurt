package com.fakap.blurt.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fakap.blurt.FriendProvider;
import com.fakap.blurt.R;
import com.fakap.blurt.activity.BlurtActivity;
import com.fakap.blurt.model.Friend;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A fragment representing a list of Friends.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FriendListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String TAG = "FriendListFragment";
    // TODO: Customize parameters

    FriendProvider friendProvider;
    BaseAdapter friendListAdapter;
    Bundle bundle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendListFragment() {
    }

    public static FriendListFragment newInstance() {
        FriendListFragment fragment = new FriendListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        fragment.setArguments(args);
        fragment.setBundle(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeList() {
        friendListAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return friendProvider.getCount();
            }

            @Override
            public Object getItem(int position) {
                return friendProvider.getFriend(position);
            }

            @Override
            public long getItemId(int position) {
                return Long.parseLong(friendProvider.getFriend(position).getId());
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.fragment_friend, parent, false);

                    CircleImageView friendPic = (CircleImageView) convertView.findViewById(
                            R.id.friend_pic_image_view);
                    TextView friendName = (TextView) convertView.findViewById(
                            R.id.friend_name_text_view);

                    final Friend friend = (Friend) getItem(position);

                    Log.d(TAG, "setting profile pic");
                    friendPic.setImageBitmap(friend.getProfilePic());
                    friendName.setText(friend.getName());

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BlurtActivity.lastContactedFriendId = friend.getId();
                        }
                    });
                }
                return convertView;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        friendProvider = new FriendProvider();
        ListView friendListView = (ListView) view.findViewById(R.id.friend_list_view);
        initializeList();
        friendProvider.setAdapter(friendListAdapter);
        friendListView.setAdapter(friendListAdapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Friend item);
    }

}
