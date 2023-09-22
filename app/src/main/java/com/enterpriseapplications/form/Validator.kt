package com.enterpriseapplications.form



class Validators
{
    companion object Factory {
        fun required(): Validator<String?> {return RequiredValidator()}
        fun minLength(minLength: Int): Validator<String?> {return MinLengthValidator(minLength)}
        fun maxLength(maxLength: Int): Validator<String?> {return MaxLengthValidator(maxLength)}
        fun min(min: Int): Validator<Int?> {return MinValidator(min)}
        fun max(max: Int): Validator<Int?> {return MaxValidator(max)}
    }
}
private class RequiredValidator : Validator<String?>(errorName = "required")
{
    override fun validate(value: String?): Boolean {
        if(value != null)
            return true;
        return false;
    }
}
private class MinLengthValidator(private val minLength: Int) : Validator<String?>(errorName = "minLength")
{
    override fun validate(value: String?): Boolean {
        if(value != null) {
            if(value.length > minLength)
                return true;
            return false;
        }
        return false;
    }
}
private class MaxLengthValidator(private val maxLength: Int): Validator<String?>(errorName = "maxLength")
{
    override fun validate(value: String?): Boolean {
        if(value != null) {
            if(value.length > maxLength)
                return true;
            return false;
        }
        return false;
    }
}
private class RangeLengthValidator(private val minLength: Int,private val maxLength: Int): Validator<String?>(errorName = "rangeLength")
{
    override fun validate(value: String?): Boolean {
        if(value != null) {
            if(value.length in (minLength + 1) until maxLength)
                return true;
        }
        return false;
    }

}
private class MinValidator(private val min: Int): Validator<Int?>(errorName = "min")
{
    override fun validate(value: Int?): Boolean {
        if(value != null)
        {
            if(value > min)
                return true;
            return false;
        }
        return false;
    }
}
private class MaxValidator(private val max: Int): Validator<Int?>(errorName = "max")
{
    override fun validate(value: Int?): Boolean {
        if(value != null)
        {
            if(value > max)
                return false;
            return false;
        }
        return true;
    }
}
abstract class Validator<T>(val errorName: String) {
    abstract fun validate(value: T?): Boolean
}