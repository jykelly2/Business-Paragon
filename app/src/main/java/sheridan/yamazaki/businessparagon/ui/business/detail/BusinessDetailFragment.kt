package sheridan.yamazaki.businessparagon.ui.business.detail


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Xml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import sheridan.yamazaki.businessparagon.R
import sheridan.yamazaki.businessparagon.databinding.BusinessDetailFragmentBinding
import java.io.File
import java.io.StringReader
import java.io.StringWriter
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

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

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
         binding.recyclerProducts.addItemDecoration(divider)

        val adapter = ProductListAdapter()
         binding.recyclerProducts.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner) { product ->
            adapter.submitList(product)
        }

        viewModel.business.observe(viewLifecycleOwner) { business ->
            binding.business = business
            if (business.logo != ""){
                Glide.with(binding.root)
                        .load(business.logo)
                        .into(binding.businessLogo)
            }else{
                binding.businessLogo.setImageResource(R.drawable.ic_launcher_background)
            }
        }

        viewModel.layout.observe(viewLifecycleOwner) { layout ->
           /* val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val builder: DocumentBuilder = factory.newDocumentBuilder()
            val d1: Document? = //builder.parse(ByteArrayInputStream(layout.toByteArray(charset("UTF-8"))))
                     builder.parse(InputSource(StringReader(layout)))
            if (d1 != null) {
                val errNodes: NodeList = d1.getElementsByTagName("LinearLayout")
                if (errNodes.getLength() > 0) {
                    val err: Element = errNodes.item(0) as Element
                } else {
                    // success
                }
                val transformer: Transformer = TransformerFactory.newInstance().newTransformer()

                // ==== Start: Pretty print
                // https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java
                transformer.setOutputProperty(OutputKeys.INDENT, "yes")
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
                // ==== End: Pretty print
                val sw = StringWriter()
               transformer.transform(DOMSource(d1), StreamResult(File("/data/user/0/sheridan.yamazaki.businessparagon/files/business_detail_fragment.xml")))
            }*/
          //  Log.d("layout", layout)
            binding.recyclerProducts.setBackgroundColor(Color.parseColor(layout.backgroundColour))
            binding.layout = layout
        }

        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}