import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetlink.R

class ClinicListAdapter(private val clinicList : List<ClinicList>) : RecyclerView.Adapter<ClinicListAdapter.ClinicViewHolder>() {

    class ClinicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val clinicImageView : ImageView = itemView.findViewById(R.id.ivClinic)
        val clinicNameTv : TextView = itemView.findViewById(R.id.tvCardClinicName)
        val clinicLocationTv : TextView = itemView.findViewById(R.id.tvClinicLocation)
        val clinicTimeOpenTv : TextView = itemView.findViewById(R.id.tvClinicTimeOpen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_clinic,parent,false)
        return ClinicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return clinicList.size
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val clinicList = clinicList[position]
        holder.clinicImageView.setImageResource(clinicList.clinicImage)
        holder.clinicNameTv.text = clinicList.clinicName
        holder.clinicLocationTv.text = clinicList.clinicLocation
        holder.clinicTimeOpenTv.text = clinicList.clinicTimeOpen
    }
}