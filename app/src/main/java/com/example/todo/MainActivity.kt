package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils.writeLines
import org.apache.commons.io.IOUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var taskList = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // Remove item from list
                taskList.removeAt(position)
                // Notify adapter for data set change
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()

        val recyclerView = findViewById<RecyclerView>(R.id.taskListView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(taskList, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager =  LinearLayoutManager(this)

        //Set up button and input field

        val userInputText = findViewById<EditText>(R.id.addTask)

        findViewById<Button>(R.id.addButton).setOnClickListener{
            // Grab user input from @+id/addTask
            val userInput = userInputText.text.toString()

            // Add string to List of task: val taskList
            taskList.add(userInput)

            // Data update notification send to adapter
            adapter.notifyItemInserted(taskList.size - 1)

            // Reset text field
            findViewById<EditText>(R.id.addTask).setText("")

            saveItems()

        }

    }

    // Save user data input
    //Save data by read and writing in a file

    // Method to get file needed
    fun getDataFile(): File {
        // Each line is a specific task in our task's list
        return File(filesDir, "data.txt")
    }

    // Load items by reading every line in the data file
    fun loadItems(){
        try {
            taskList = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }

    }

    // Save items by writing them into our data file
    fun saveItems(){
        try{
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), taskList)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

}