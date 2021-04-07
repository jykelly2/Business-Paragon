package sheridan.yamazaki.businessparagon.ui.business.detail

import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.w3c.dom.Document
import org.xml.sax.InputSource
import sheridan.yamazaki.businessparagon.databinding.BusinessDetailFragmentBinding
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

@AndroidEntryPoint
class BusinessDetailFragment: Fragment() {

    private lateinit var binding: BusinessDetailFragmentBinding
    private val viewModel: BusinessDetailViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val id = requireArguments().getString("id")

        if (id != null) {
            viewModel.loadData(id)
            viewModel.returnLayout(id, "detail")
        }


        binding = BusinessDetailFragmentBinding.inflate(inflater, container, false)

        viewModel.business.observe(viewLifecycleOwner) { business ->
            binding.business = business
        }

        viewModel.layout.observe(viewLifecycleOwner) { layout ->
            val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val builder: DocumentBuilder = factory.newDocumentBuilder()
            val d1: Document? = builder.parse(InputSource(StringReader(layout)))
            if (d1 != null) {
                Log.d("juufn", d1.toString())
            }
            Log.d("juufh", layout)
        }

        return binding.root
    }
}