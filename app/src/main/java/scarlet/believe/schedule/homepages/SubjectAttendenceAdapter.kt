package scarlet.believe.schedule.homepages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.attendenceview_card.view.*
import scarlet.believe.schedule.R

class SubjectAttendenceAdapter(val subjects : MutableList<Int>,val timelist : MutableList<String>,val colorlist : ArrayList<Int>) : RecyclerView.Adapter<SubjectAttendenceAdapter.SubjectViewHolder>(){

    var i = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.attendenceview_card,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = subjects.size

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {

        holder.view.text_present_attend.text = subjects[position].toString()
        holder.view.text_all_sub.text = timelist[position]
        holder.view.card_attend.setCardBackgroundColor(colorlist[(i++)%5])
    }

    class SubjectViewHolder(val view : View) : RecyclerView.ViewHolder(view)
}