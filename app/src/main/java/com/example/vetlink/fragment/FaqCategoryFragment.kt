package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vetlink.R
import com.example.vetlink.adapter.FaqList
import com.example.vetlink.adapter.FaqListAdapter
import com.example.vetlink.databinding.FragmentFaqCategoryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FaqCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FaqCategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var data: String? = null
    private lateinit var binding: FragmentFaqCategoryBinding
    private lateinit var faqList: ArrayList<FaqList>
    private lateinit var faqListAdapter: FaqListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            data = it.getString("key")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaqCategoryBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        with(binding){
            val tvDesc = tvFAQCategoryDesc.layoutParams as ViewGroup.MarginLayoutParams

            rvFAQCategory.layoutManager = LinearLayoutManager(requireContext())
            val isFaqCategory = true

            if(data == "Panduan"){
                ivFAQCategory.setImageResource(R.drawable.img_guide)
                tvFAQCategoryHeader.text = "Panduan Menggunakan Aplikasi"
                tvFAQCategoryDesc.text = "VetLink adalah aplikasi yang dirancang untuk memudahkan " +
                        "pemilik hewan peliharaan dalam menemukan klinik hewan, melakukan reservasi " +
                        "kunjungan, serta berinteraksi di forum komunitas."
                tvDesc.setMargins(0, 15, 0, 0)

                faqList = ArrayList()
                addDataToFAQPanduan()

                faqListAdapter = FaqListAdapter(faqList, isFaqCategory)
                rvFAQCategory.adapter = faqListAdapter

            }
            else if (data == "Pembatalan"){
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)

                ivFAQCategory.setImageResource(R.drawable.img_cancel)
                tvFAQCategoryHeader.text = "Pembatalan Kunjungan Klinik"

                faqList = ArrayList()
                addDataToFAQPembatalan()

                faqListAdapter = FaqListAdapter(faqList, isFaqCategory)
                rvFAQCategory.adapter = faqListAdapter

                tvFAQCategoryDesc.text = "Notifikasi Pembatalan: Klinik terkait akan menerima " +
                        "notifikasi pembatalan Anda secara otomatis, dan Anda juga akan mendapatkan " +
                        "pemberitahuan bahwa kunjungan telah dibatalkan."

                constraintSet.connect(
                    R.id.tvFAQCategoryDesc,
                    ConstraintSet.TOP,
                    R.id.rvFAQCategory,
                    ConstraintSet.BOTTOM
                )

                constraintSet.connect(
                    R.id.rvFAQCategory,
                    ConstraintSet.TOP,
                    R.id.tvFAQCategoryHeader,
                    ConstraintSet.BOTTOM
                )

                constraintSet.clear(
                    R.id.rvFAQCategory,
                    ConstraintSet.BOTTOM
                )

                constraintSet.applyTo(constraintLayout)


            }
            else if (data == "Publikasi"){

                ivFAQCategory.setImageResource(R.drawable.img_how_to_post)
                tvFAQCategoryHeader.text = "Publikasi Kehilangan Hewan"
                tvFAQCategoryDesc.text = "VetLink memiliki fitur Forum yang memungkinkan Anda untuk " +
                        "melaporkan hewan peliharaan yang hilang dan membantu orang lain dalam menemukannya. " +
                        "Dengan fitur ini, Anda dapat membuat postingan yang mencakup informasi penting seperti " +
                        "deskripsi hewan dan foto hewan tersebut."
                tvDesc.setMargins(0, 15, 0, 0)

                faqList = ArrayList()
                addDataToPublikasi()

                faqListAdapter = FaqListAdapter(faqList, isFaqCategory)
                rvFAQCategory.adapter = faqListAdapter

            }
        }
    }

    private fun addDataToFAQPanduan(){
        faqList.add(FaqList("1. Reservasi Kunjungan",
            "Anda dapat mencari dan memilih klinik hewan terdekat, melihat jadwal dokter, " +
                    "dan membuat reservasi kunjungan langsung melalui aplikasi. Terdapat juga opsi " +
                    "untuk membatalkan atau mengubah jadwal jika diperlukan."))

        faqList.add(FaqList("2. Forum Kehilangan Hewan",
            "Forum ini memungkinkan Anda untuk membuat postingan tentang hewan yang hilang " +
                    "atau memberikan informasi jika Anda menemukan hewan. Anda juga bisa membalas " +
                    "atau berinteraksi dengan postingan lain untuk membantu pengguna lain dalam " +
                    "menemukan hewan peliharaan mereka."))

        faqList.add(FaqList("3. Pengelolaan Postingan",
            "Semua postingan yang pernah Anda buat dapat dikelola melalui menu My Board, " +
                    "di mana Anda dapat menandai postingan sebagai selesai atau melihat riwayat " +
                    "postingan sebelumnya."))

        faqList.add(FaqList("4. Riwayat Kunjungan",
            "Fitur ini memungkinkan Anda untuk melihat catatan kunjungan ke klinik hewan " +
                    "yang pernah Anda lakukan, sehingga Anda dapat memantau kesehatan hewan " +
                    "peliharaan dengan lebih baik."))

        faqList.add(FaqList("5. Pembaruan Fitur",
            "Beberapa fitur tambahan, seperti memberi rating atau ulasan klinik," +
                    "sedang dikembangkan dan akan segera tersedia dalam pembaruan mendatang."))
    }

    private fun addDataToFAQPembatalan(){
        faqList.add(FaqList("1. Masuk ke Aplikasi VetLink",
            "Buka aplikasi dan login ke akun Anda."))

        faqList.add(FaqList("2. Akses Menu Profile dan Pilih Menu Schedule",
            "Periksa daftar kunjungan yang telah anda pesan."))

        faqList.add(FaqList("3. Periksa Bagian Upcoming",
            "Pilih kunjungan yang ingin dibatalkan dengan tekan lama pada kunjungan tersebut."))

        faqList.add(FaqList("4. Klik Opsi Batalkan Kunjungan",
            "Anda akan diminta untuk mengonfirmasi pembatalan, setelah dikonfirmasi kunjungan tersebut akan dibatalkan."))
    }

    private fun addDataToPublikasi(){
        faqList.add(FaqList("1. Membuat Postingan",
            "Masuk ke menu \"Forum\" dan tekan tanda tambah untuk membuat postingan baru." +
                    "Isi detail mengenai hewan yang hilang, termasuk ciri-ciri fisik, lokasi, dan " +
                    "waktu terakhir terlihat. Jangan lupa untuk menambahkan foto agar lebih mudah dikenali."))

        faqList.add(FaqList("2. Berinteraksi dengan Pengguna Lain",
            "Anda dapat membalas komentar atau pertanyaan dari pengguna lain yang mungkin" +
                    "memiliki informasi terkait keberadaan hewan. Fitur ini memudahkan komunikasi " +
                    "dan meningkatkan peluang hewan ditemukan."))

        faqList.add(FaqList("3. Menandai Postingan sebagai Selesai",
            "Jika hewan Anda telah ditemukan, Anda bisa menandai postingan sebagai \"Selesai\" " +
                    "untuk memberi tahu komunitas bahwa masalah sudah terselesaikan."))
    }

    companion object {
        private lateinit var arguments: Bundle

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter   1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FaqCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(data: String): FaqCategoryFragment {
            val fragment = FaqCategoryFragment()
            val bundle = Bundle()
            bundle.putString("key", data)
            fragment.arguments = bundle
            return fragment
        }

    }
}