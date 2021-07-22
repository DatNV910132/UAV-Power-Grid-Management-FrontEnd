package com.dji.sdk.sample.ui.fragment.mission;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dji.sdk.sample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MissionManagerFragment extends Fragment {

    /**
     * Instantiates a new Mission manager fragment.
     */
    public MissionManagerFragment() {
        // Required empty public constructor
    }

    private ImageView img_mision;
    private ImageView img_papmission;
    private ImageView img_tempmission;
    private TextView tv_mission;
    private TextView tv_papmission;
    private TextView tv_tempmission;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_mission_manager, container, false);

        img_mision = root.findViewById(R.id.img_btn_mision);
        img_papmission = root.findViewById(R.id.img_btn_papmision);
        img_tempmission = root.findViewById(R.id.img_btn_temppolemision);
        tv_mission = root.findViewById(R.id.tv_btn_mission);
        tv_papmission = root.findViewById(R.id.tv_btn_papmission);
        tv_tempmission = root.findViewById(R.id.tv_btn_temppolemission);


        img_mision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new MissionFragment());
                ft_rep.commit();
            }
        });

        tv_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new MissionFragment());
                ft_rep.commit();
            }
        });

        img_papmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new ChildMissionFragment());
                ft_rep.commit();
            }
        });

        tv_papmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new ChildMissionFragment());
                ft_rep.commit();
            }
        });

        img_tempmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new TemplatePoleMissionFragment());
                ft_rep.commit();
            }
        });

        tv_tempmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft_rep = fm.beginTransaction();
                ft_rep.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ft_rep.replace(R.id.nav_host_fragment, new TemplatePoleMissionFragment());
                ft_rep.commit();
            }
        });

        return root;
    }
}
