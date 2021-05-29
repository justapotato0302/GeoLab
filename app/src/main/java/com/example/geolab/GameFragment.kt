package com.example.geolab


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.example.geolab.GameVIewModel
import com.example.geolab.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.lang.StringBuilder
import java.util.*
import kotlin.properties.Delegates

class GameFragment : Fragment() {

    private var _binding : FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var map_name : String
    private lateinit var mDatabase : DatabaseReference
    private var currentQuestion = 2

    private var randomInt by Delegates.notNull<Int>()
    private var pickedInt: MutableSet<Int> = mutableSetOf()
    private lateinit var myRadioGroup : RadioGroup

    private val viewModel : GameVIewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            map_name = it.getString("map_name2").toString()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mDatabase = FirebaseDatabase.getInstance().reference.child("Map").child(map_name).child("Question")
        val mapListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("GameFragment", "loadPost:onCancelled", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Picasso.get().load(snapshot.child("Q1").child("Image").getValue<String>()).into(binding.gameImage)
                binding.test.text = snapshot.child("Q1").child("Question").getValue<String>()
                binding.radioButton1.text = snapshot.child("Q1").child(ANSWER_FORMAT + rand(1,5)).getValue<String>()
                binding.radioButton2.text = snapshot.child("Q1").child(ANSWER_FORMAT + rand(1,5)).getValue<String>()
                binding.radioButton3.text = snapshot.child("Q1").child(ANSWER_FORMAT + rand(1,5)).getValue<String>()
                binding.radioButton4.text = snapshot.child("Q1").child(ANSWER_FORMAT + rand(1,5)).getValue<String>()
            }
        }
        mDatabase.addValueEventListener(mapListener)
        myRadioGroup = binding.radioGroup
        binding.submit.setOnClickListener{
            pickedInt.clear()
            val question = "Q".plus(currentQuestion)
            val radioBtn: RadioButton =
                myRadioGroup.findViewById(myRadioGroup.checkedRadioButtonId)
            mDatabase.child("Q".plus(currentQuestion - 1)).child("Answer1").get()
                .addOnSuccessListener {
                    if (radioBtn.text == it.value) {
                        viewModel.increaseScore()
                    }
                }
            if (viewModel.currentQuestionCount.value!! == 5){
                showFinalScoreDialog()
            } else if (myRadioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(requireContext(), "Please select an answer" , Toast.LENGTH_SHORT).show()
            } else {
                mDatabase.child(question).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Picasso.get().load(snapshot.child("Image").getValue<String>())
                            .into(binding.gameImage)
                        binding.test.text = snapshot.child("Question").getValue<String>()
                        binding.radioButton1.text =
                            snapshot.child(ANSWER_FORMAT + rand(1, 5)).getValue<String>()
                        binding.radioButton2.text =
                            snapshot.child(ANSWER_FORMAT + rand(1, 5)).getValue<String>()
                        binding.radioButton3.text =
                            snapshot.child(ANSWER_FORMAT + rand(1, 5)).getValue<String>()
                        binding.radioButton4.text =
                            snapshot.child(ANSWER_FORMAT + rand(1, 5)).getValue<String>()
                    }

                })
                ++currentQuestion
                viewModel.increaseCurrentQuestionCount()
                Log.d("GameFragment", "Question : ${question}")
                Log.d("GameFragment", "Score : ${viewModel.score}")
                Log.d("GameFragment", "Score : ${viewModel.currentQuestionCount}")
            }
        }
        viewModel.currentQuestionCount.observe(viewLifecycleOwner
        ) { newWordCount ->
            binding.wordCount.text =
                getString(R.string.word_count, newWordCount, MAX_NO_OF_QUESTION)
        }
        viewModel.score.observe(viewLifecycleOwner
        ) { newScore ->
            binding.score.text = getString(R.string.score, newScore)
        }
    }

    private fun rand(from: Int, to: Int): Int{
        do{
            randomInt = Random().nextInt(to - from) + from
        }while(pickedInt.contains(randomInt))

        pickedInt.add(randomInt)
        return randomInt
    }

    private fun exitGame() {
        activity?.finish()
    }
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value?.plus(10))) //TODO remember to fix this later.
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                viewModel.reinitializeData()
                val action = GameFragmentDirections.actionGameFragmentToMainActivityFragment()
                view?.findNavController()?.navigate(action)
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}