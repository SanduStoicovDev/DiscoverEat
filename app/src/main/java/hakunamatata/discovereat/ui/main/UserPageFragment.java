package hakunamatata.discovereat.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import hakunamatata.discovereat.R;

import hakunamatata.discovereat.data.repository.user.IUserRepository;
import hakunamatata.discovereat.databinding.FragmentUserPageBinding;
import hakunamatata.discovereat.ui.login.UserViewModel;
import hakunamatata.discovereat.ui.login.UserViewModelFactory;
import hakunamatata.discovereat.util.ServiceLocator;

import com.google.android.material.progressindicator.LinearProgressIndicator;


public class UserPageFragment extends Fragment {
    private static final String TAG = UserPageFragment.class.getSimpleName();
    private FragmentUserPageBinding fragmentUserPageBinding;
    private UserViewModel userViewModel;
    private LinearProgressIndicator progressIndicator;

    public UserPageFragment() {
        // Required empty public constructor
    }
    public static UserPageFragment newInstance() {
        return new UserPageFragment();
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(
                requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user_page, container, false);
        fragmentUserPageBinding = FragmentUserPageBinding.inflate(inflater, container, false);
        return fragmentUserPageBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        progressIndicator = view.findViewById(R.id.progress_bar);

        ImageView imageViewUserPage = view.findViewById(R.id.imageView_UserPage);

        imageViewUserPage.setImageDrawable(AppCompatResources.getDrawable(this.getContext(),
                R.drawable.icona_user));
        fragmentUserPageBinding.buttonLogout.setOnClickListener(v -> {
            progressIndicator.setVisibility(View.VISIBLE);
            userViewModel.logout().observe(getViewLifecycleOwner(), result -> {
                if (result.isSuccess()) {
                    Navigation.findNavController(view).navigate(
                            R.id.action_userPageFragment_to_loginActivity);
                } else {
                    Snackbar.make(view,
                            requireActivity().getString(R.string.unexpected_error),
                            Snackbar.LENGTH_SHORT).show();
                }
                progressIndicator.setVisibility(View.GONE);
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentUserPageBinding = null;
    }
}

