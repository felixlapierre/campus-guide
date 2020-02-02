import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity
data class Point(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name="latlng") val latLng: LatLng,
    @ColumnInfo(name="order") val order: Int
)