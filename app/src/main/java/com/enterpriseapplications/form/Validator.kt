package com.enterpriseapplications.form

import java.math.BigInteger


class Validators
{
    companion object Factory {
        fun required(): Validator<String?> {return RequiredValidator()}
        fun minLength(minLength: Int): Validator<String?> {return MinLengthValidator(minLength)}
        fun maxLength(maxLength: Int): Validator<String?> {return MaxLengthValidator(maxLength)}
        fun min(min: BigInteger): Validator<String?> {return MinValidator(min)}
        fun max(max: BigInteger): Validator<String?> {return MaxValidator(max)}
    }
}
private class RequiredValidator : Validator<String?>(errorName = "required", errorText = "Value can't be null or empty")
{
    override fun validate(value: String?): Boolean {
        if(!value.isNullOrEmpty())
            return true;
        return false;
    }
}
private class MinLengthValidator(private val minLength: Int) : Validator<String?>(errorName = "minLength", errorText = "Value's length must be greater than $minLength")
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
private class MaxLengthValidator(private val maxLength: Int): Validator<String?>(errorName = "maxLength", errorText = "Value's length must be less than $maxLength")
{
    override fun validate(value: String?): Boolean {
        if(value != null) {
            if(value.length < maxLength)
                return true;
            return false;
        }
        return false;
    }
}
private class RangeLengthValidator(private val minLength: Int,private val maxLength: Int): Validator<String?>(errorName = "rangeLength", errorText = "Value's length must be between $minLength and $maxLength")
{
    override fun validate(value: String?): Boolean {
        if(value != null) {
            if(value.length in (minLength + 1) until maxLength)
                return true;
        }
        return false;
    }

}
private class MinValidator(private val min: BigInteger): Validator<String?>(errorName = "min", errorText = "Value must be greater than $min")
{
    override fun validate(value: String?): Boolean {
        if(value != null)
        {
            val requiredValue = value.toBigIntegerOrNull()
            if(requiredValue != null) {
                if(requiredValue > min)
                    return true;
            }
            return false;
        }
        return false;
    }
}
private class MaxValidator(private val max: BigInteger): Validator<String?>(errorName = "max", errorText = "Value must be less than $max")
{
    override fun validate(value: String?): Boolean {
        if(value != null)
        {
            val requiredValue = value.toBigIntegerOrNull();
            if(requiredValue != null) {
                if(requiredValue < max)
                    return true;
            }
            return false;
        }
        return true;
    }
}
abstract class Validator<T>(val errorName: String,val errorText: String) {
    abstract fun validate(value: T?): Boolean
}