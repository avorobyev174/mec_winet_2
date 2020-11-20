package com.avorobyev174.mec_winet.classes.common;

import androidx.fragment.app.Fragment;

public abstract class DialogFragment extends Fragment {
    private Entity entity;

    public interface fragmentShowListner {
        public void onChangeEntity(Entity entity);
    }

    abstract public void showEntityCreateDialog();

    public void saveEntityInfoDialog() {

    };

//    public static DialogFragment newInstance() {
//        return null;
//    };
}
