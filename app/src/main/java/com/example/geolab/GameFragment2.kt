package com.example.geolab

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.example.geolab.databinding.FragmentGame2Binding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.properties.Delegates


class GameFragment2 : Fragment() {
    private var _binding : FragmentGame2Binding? = null
    private val binding get() = _binding!!

    private lateinit var section_name : String
    private lateinit var mDatabase : DatabaseReference
    private lateinit var userDatabase: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private var currentQuestion = 2

    private var randomInt by Delegates.notNull<Int>()
    private var pickedInt: MutableSet<Int> = mutableSetOf()
    private lateinit var myRadioGroup : RadioGroup

    private val viewModel : GameVIewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            section_name = it.getString("section").toString()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGame2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val delimeter = "_"
        val list : List<String> = section_name.split(delimeter)
        auth = FirebaseAuth.getInstance()
        val uid : String = auth.currentUser.uid
        userDatabase = FirebaseDatabase.getInstance().reference.child("users").child(uid)
        mDatabase = FirebaseDatabase.getInstance().reference.child("Map2").child(list[0]).child("Section").child(list[1]).child("Question")
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
                userDatabase.get().addOnSuccessListener {
                    if (section_name.equals(it.child("currentStage").getValue<String>())) {
                        val highscore: Int = it.child("highScore").getValue<String>()!!.toInt()
                        if (viewModel.score.value!! >= highscore) {
                            userDatabase.child("highScore")
                                .setValue(viewModel.score.value!!.toString())
                            if (viewModel.score.value!! == MAX_SCORE) {
                                userDatabase.child("highScore")
                                    .setValue("0")
                                userDatabase.child("currentStage").setValue(nextChapter(section_name))
                            }
                        }
                    }
                }
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

    private fun nextChapter(currentStage : String) : String {
        val list : CharArray = currentStage.toCharArray()
        val stageInNumber : String = list[7].toString() + list[12].toString()
        var nextChapter = stageInNumber.toInt() / 10
        var nextSection = stageInNumber.toInt() % 10
        if (nextSection < 4) {
            nextSection += 1
            return "CHAPTER" + nextChapter + "_sec" + nextSection
        } else {
            nextChapter += 1
            nextSection = 1
            return "CHAPTER" + nextChapter + "_sec" + nextSection
        }
    }
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                viewModel.reinitializeData()
                val action = GameFragment2Directions.actionGameFragment2ToMainActivity2Fragment()
                view?.findNavController()?.navigate(action)
            }
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}