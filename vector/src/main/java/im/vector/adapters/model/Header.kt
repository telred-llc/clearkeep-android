package im.vector.adapters.model

class Header constructor(private var header: String,private var number : String,private var imgIcon : Int) {

    fun getHeaderName() : String {
        return header
    }

    fun getNumber() : String {
        return number
    }

    fun getImgIcon() : Int {
        return imgIcon
    }

}