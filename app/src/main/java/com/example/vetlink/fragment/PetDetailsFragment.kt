package com.example.vetlink.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R
import com.example.vetlink.adapter.PetsCategoryList
import com.example.vetlink.adapter.PetsCategoryListAdapter
import com.example.vetlink.databinding.FragmentPetDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PetDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PetDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPetDetailsBinding

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
        binding = FragmentPetDetailsBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView(){

        with(binding){

            btnSubmitPets.setOnClickListener{
                val namePets = etNamePets.text.toString()
                val agePets = etAgePets.text.toString()
                val weightPets = etWeightPets.text.toString()
                val colorError = ColorStateList.valueOf(Color.RED)

                if(namePets.isEmpty()){
                    textInputLayoutNamePets.error = "Name is required"
                    textInputLayoutNamePets.setErrorTextColor(colorError)
                    textInputLayoutNamePets.setErrorIconTintList(colorError)
                    etNamePets.requestFocus()
                } else {
                    textInputLayoutNamePets.error = null
                    textInputLayoutNamePets.isErrorEnabled = false
                }

                if(agePets.isEmpty()){
                    textInputLayoutAgePets.error = "Age is required"
                    textInputLayoutAgePets.setErrorTextColor(colorError)
                    textInputLayoutAgePets.setErrorIconTintList(colorError)
                    etAgePets.requestFocus()
                } else {
                    textInputLayoutAgePets.error = null
                    textInputLayoutAgePets.isErrorEnabled = false
                }

                if(weightPets.isEmpty()){
                    textInputLayoutWeightPets.error = "Weight is required"
                    textInputLayoutWeightPets.setErrorTextColor(colorError)
                    textInputLayoutWeightPets.setErrorIconTintList(colorError)
                    etWeightPets.requestFocus()
                } else {
                    textInputLayoutWeightPets.error = null
                    textInputLayoutWeightPets.isErrorEnabled = false
                }
            }

            btnChoosePet.setOnClickListener{
                val title = "Choose Pet"
                showButtonSheetCategoryDialog(title)
            }

            btnChooseBreed.setOnClickListener{
                val title = "Choose Breed"
                showButtonSheetCategoryDialog(title)
            }

            etNotePets.addTextChangedListener(object : TextWatcher{
                private var isLineBreakAllowed = false

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No action needed before text change
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Check if the first line is complete
                    if (!isLineBreakAllowed && s?.contains("\n") == true) {
                        // If a line break is detected before the first line is complete, remove it
                        val newText = s.toString().replace("\n", "")
                        etNotePets.setText(newText)
                        etNotePets.setSelection(newText.length) // Move cursor to the end
                    } else if (etNotePets.layout != null) {
                        // Check if the text exceeds the first line width
                        if (etNotePets.layout.lineCount > 1 || (etNotePets.text?.length
                                ?: 0) >= etNotePets.width
                        ){
                            isLineBreakAllowed = true
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // No action needed after text change
                }
            })

        }
    }

    @SuppressLint("InflateParams")
    private fun showButtonSheetCategoryDialog(title: String){


//        dialog
        val dialog = activity?.let { BottomSheetDialog(it) }
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_pet_dialog, null, false)

//        Inisialisasi view
        val headerDialog = view.findViewById<TextView>(R.id.tvDialog)
        val reyclerView = view.findViewById<RecyclerView>(R.id.rvPetCategoryList)

        reyclerView.layoutManager = LinearLayoutManager(requireContext())

//        Data reycler view
        val petsCategoryList: ArrayList<PetsCategoryList> = ArrayList()

        if (title == "Choose Pet") {
            headerDialog.text = title
            addDataPetsCategoryToList(petsCategoryList)
        } else if (title == "Choose Breed"){
            headerDialog.text = title
            addDataBreedToList(petsCategoryList)
        }

        val petsCategoryListAdapter = PetsCategoryListAdapter(petsCategoryList)
        reyclerView.adapter = petsCategoryListAdapter


//        Buat Dialog
        dialog?.apply {
            setCancelable(true)
            setContentView(view)

            show()

//            Bottomsheet height & dragable
            val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isHideable = true

        }

    }

    private fun addDataPetsCategoryToList(petsCategory: ArrayList<PetsCategoryList>){
        petsCategory.add(PetsCategoryList("Dogs"))
        petsCategory.add(PetsCategoryList("Cats"))
    }

    private fun addDataBreedToList(petsCategory: ArrayList<PetsCategoryList>){
        petsCategory.add(PetsCategoryList("Persian"))
        petsCategory.add(PetsCategoryList("Ragdoll"))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PetDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PetDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}