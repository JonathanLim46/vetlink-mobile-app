package com.example.vetlink.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.activity.MenuActivity
import com.example.vetlink.adapter.FaqCategoryList
import com.example.vetlink.adapter.FaqCategoryListAdapter
import com.example.vetlink.adapter.FaqList
import com.example.vetlink.adapter.FaqListAdapter
import com.example.vetlink.adapter.RecyclerViewClickListener
import com.example.vetlink.databinding.FragmentFaqBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FaqFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FaqFragment : Fragment(), RecyclerViewClickListener<FaqCategoryList>{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentFaqBinding
    private lateinit var faqList: ArrayList<FaqList>
    private lateinit var faqListAdapter: FaqListAdapter
    private lateinit var faqCategoryList: ArrayList<FaqCategoryList>
    private lateinit var faqCategoryListAdapter: FaqCategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaqBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView(){
        with(binding){

            rvFAQ.layoutManager = LinearLayoutManager(requireContext())

            faqList = ArrayList()
            addDataToFaq()

            val isFaqCategory = false
            faqListAdapter = FaqListAdapter(faqList, isFaqCategory)
            rvFAQ.adapter = faqListAdapter


            rvFAQIV.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            faqCategoryList = ArrayList()
            addDataToFAQIV()
            faqCategoryListAdapter = FaqCategoryListAdapter(faqCategoryList)
            rvFAQIV.adapter = faqCategoryListAdapter

            faqCategoryListAdapter.clickListener(this@FaqFragment)
        }
    }

    private fun addDataToFAQIV() {
        faqCategoryList.add(
            FaqCategoryList(
                R.drawable.img_guide,
                "Panduan \n Menggunakan \n Aplikasi",
                "Panduan Aplikasi"
            )
        )
        faqCategoryList.add(
            FaqCategoryList(
                R.drawable.img_cancel,
                "Pembatalan \n Kunjungan \n Klinik",
                "FAQ Pembatalan"
            )
        )
        faqCategoryList.add(
            FaqCategoryList(
                R.drawable.img_how_to_post,
                "Publikasi \n Kehilangan \n Hewan",
                "FAQ Kehilangan"
            )
        )
    }

    private fun addDataToFaq(){
        faqList.add(FaqList("Bagaimana cara membalas atau berinteraksi dengan postingan lain di Forum ?",
                "Untuk membalas atau berinteraksi dengan postingan di Forum VetLink, buka aplikasi dan pilih menu " +
                        "Forum, lalu cari postingan yang relevan. Tekan ikon komentar di bawah postingan untuk membuka kolom " +
                        "komentar, tulis balasan Anda di kotak yang tersedia, dan tekan tombol Kirim untuk memposting komentar tersebut"))

        faqList.add(FaqList("Bagaimana cara menandai postingan di forum yang sudah ditemukan atau tidak relevan lagi?",
            "Untuk menandai postingan Anda di forum VetLink sebagai sudah ditemukan atau tidak relevan, buka aplikasi dan pilih menu " +
                    "Forum, lalu akses My Board untuk melihat postingan yang pernah Anda buat. Cari postingan yang ingin ditandai, tekan " +
                    "ikon titik tiga di pojok kanan atas, dan pilih Selesaikan Postingan untuk menandainya sebagai selesai."))

        faqList.add(FaqList("Apakah reservasi kunjungan memerlukan biaya tambahan?",
            "Tidak, reservasi kunjungan tidak memungut biaya. Seluruh aktivitas aplikasi sudah dibebani kepada Klinik Hewan yang terdaftar."))

        faqList.add(FaqList("Bagaimana cara memberi rating atau ulasan untuk klinik hewan yang pernah saya kunjungi?",
            "Saat ini, fitur tersebut masih dalam tahap pengembangan. Kami akan menghadirkan fitur ini secara bertahap dan akan diinformasikan melalui informasi pembaruan aplikasi."))

        faqList.add(FaqList("Apakah saya bisa mengunduh bukti reservasi atau konfirmasi kunjungan dari aplikasi?",
            "Proses kunjungan tidak memerlukan bukti reservasi atau konfirmasi kunjungan, karena sudah diurus oleh pihak Klinik Hewan yang Anda tuju."))

        faqList.add(FaqList("Bagaimana cara menemukan kembali postingan saya di forum jika sudah lama dibuat?",
            "Untuk melihat kembali postingan yang pernah Anda buat di VetLink, buka aplikasi dan pilih menu Forum, " +
                    "lalu akses opsi My Board untuk menampilkan seluruh postingan Anda. Tekan tombol Postingan Terselesaikan " +
                    "untuk melihat daftar postingan yang sudah ditandai sebagai selesai atau tidak relevan."))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FaqFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FaqFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClicke(view: View, item: FaqCategoryList) {
        val intent = Intent(activity, MenuActivity::class.java)
        intent.putExtra("MENU_TITLE", item.faqContext)
        startActivity(intent)
    }

}