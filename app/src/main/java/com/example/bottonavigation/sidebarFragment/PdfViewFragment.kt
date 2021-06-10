package com.example.bottonavigation.sidebarFragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.example.bottonavigation.R
import com.example.bottonavigation.util.FileOpen
import com.github.barteksc.pdfviewer.PDFView
import java.io.File
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PDF_URL = "pdf_url"

class PdfViewFragment : BaseSideFragment() {
    // TODO: Rename and change types of parameters
    private var pdf_url: String? = null
    override val titleId: Int = R.string.frag_title_pdf
    private val pdf_view by bindView<PDFView>(R.id.pdf_view)

    companion object {
        /**
         * @param pdf_url Parameter 1.
         * @return A new instance of fragment PdfViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(pdf_url: String) =
            PdfViewFragment().apply {
                arguments = Bundle().apply {
                    putString(PDF_URL, pdf_url)
                }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pdf_url = it.getString(PDF_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.side_fragment_pdf_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val uri = Uri.parse("android.resource://com.example.bottonavigation/"+ R.raw.news1)
//        Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show()
        pdf_view.fromAsset(pdf_url)
            .load()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pdf_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.openWithExt) {
            val file = getFileFromAssets(context!!, pdf_url!!)
            FileOpen.openFile(context, file)
        }
        return super.onOptionsItemSelected(item)
    }
    @Throws(IOException::class)
    fun getFileFromAssets(context: Context, fileName: String): File = File(context.cacheDir, fileName)
        .also {
            it.outputStream().use { cache -> context.assets.open(fileName).use { it.copyTo(cache) } }
        }

}
