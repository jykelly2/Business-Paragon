package sheridan.yamazaki.businessparagon.ui.business

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sheridan.yamazaki.businessparagon.databinding.FragmentExploreBinding
import sheridan.yamazaki.businessparagon.databinding.FragmentLoginBinding

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }
}