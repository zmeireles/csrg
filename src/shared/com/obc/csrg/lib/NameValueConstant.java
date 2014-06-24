package com.obc.csrg.lib;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;

import com.obc.csrg.util.StringUtil;

public abstract class NameValueConstant implements Serializable{

	private static final long serialVersionUID = 551291755021091988L;

    private static final HashMap<String, HashMap<Integer, NameValueConstant>> _classToTypeMap =
        new HashMap<String, HashMap<Integer, NameValueConstant>>();

    private static final HashMap<String, HashMap<String, NameValueConstant>> _classToNameMap =
        new HashMap<String, HashMap<String, NameValueConstant>>();

    private int    _type = -1;
    private String _name = null;

    /**
     * Basic constructor.
     *
     * @param type The enumeration value for the constant.
     * @param name The description of the constant.
     *
     * @throws IllegalArgumentException If the given name is missing.  Also, if either the type or name already
     *                                  exists for another static constant within the <code>this</code> class.
     */
    protected NameValueConstant(final int type, final String name)
    {
        if((name == null) || (name.trim().length() == 0))
        {
            throw new IllegalArgumentException("Missing Constant Name");
        }

        _type = type;
        _name = name;

        // -------------------------------------------------------------
        // Keep track of the "int - type" to "NameValueConstant" mapping
        HashMap<Integer, NameValueConstant> typeMap = _classToTypeMap.get(getClassKey(getClass()));
        if(typeMap == null)
        {
            typeMap = new HashMap<Integer, NameValueConstant>();
            _classToTypeMap.put(getClassKey(getClass()), typeMap);
        }
        final Object existingType = typeMap.get(type);
        if(existingType != null)
        {
            throw new IllegalArgumentException("Type Already Exists: " + existingType);
        }
        else
        {
            typeMap.put(type, this);
        }

        // -------------------------------------------------------------
        // Keep track of the "String - name" to "NameValueConstant" mapping
        HashMap<String, NameValueConstant> nameMap = _classToNameMap.get(getClassKey(getClass()));
        if(nameMap == null)
        {
            nameMap = new HashMap<String, NameValueConstant>();
            _classToNameMap.put(getClassKey(getClass()), nameMap);
        }
        final Object existingName = nameMap.get(name.toUpperCase());
        if(existingName != null)
        {
            throw new IllegalArgumentException("Name Already Exists: " + existingName);
        }
        else
        {
            nameMap.put(name.toUpperCase(), this);
        }
    }

    public final int getType() { return(_type); }
    public final String getName() { return(_name); }

    /**
     * Returns the concrete instance of the strongly typed constant in the given class that represents the given type.
     *
     * @param childClass The class in which this constant name resides.
     * @param type       The type/enumeration for the desired constant.
     *
     * @return The concrete instance of the constant.
     */
    protected static <T extends NameValueConstant>T valueOf(final Class<T> childClass, final int type)
    {
        T returnValue = null;

        final HashMap<Integer, NameValueConstant> typeMap = _classToTypeMap.get(getClassKey(childClass));

        if(typeMap != null)
        {
            returnValue = (T) typeMap.get(type);
        }

        return(returnValue);
    }

    /**
     * Returns the concrete instance of the strongly typed constant in the given class that represents the given name.
     *
     * @param childClass The class in which this constant name resides.
     * @param name       The name/description for the desired constant.
     *
     * @return The concrete instance of the constant.
     */
    protected static <T extends NameValueConstant>T valueOf(final Class<T> childClass, final String name)
    {
        T returnValue = null;

        final HashMap<String, NameValueConstant> nameMap = _classToNameMap.get(getClassKey(childClass));

        if((name != null) && (nameMap != null))
        {
            returnValue = (T) nameMap.get(name.toUpperCase());
        }

        return returnValue;
    }

    /**
     * This method returns an appropriate key for the particular subclass of name value constant. We want all anonymous subclasses
     * of a particular concrete subclass to share the same key, so we take everything up to but not including the first '$'
     *
     * <p>
     * For example, <code>com.abc.xyz.SomeConstant$1</code> will return "com.abc.xyz.SomeConstant".
     *
     * @param childClass The class of the name value constant instance.
     *
     * @return The key used in internal maps of instances.
     */
    private static String getClassKey(final Class<?> childClass)
    {
        String key = null;

        if(childClass != null)
        {
            key = childClass.getName();
            final int dollarIndex = key.indexOf((int) '$');
            if(dollarIndex >= 0)
            {
                key = key.substring(0, dollarIndex);
            }
        }

        return key;
    }

    /**
     * Creates a set of instances of constants from the specified constants class.
     *
     * <p>
     * Note: This looks for <code>public static final</code> instance delarations of the declaring class.
     *
     * @param clazz The declaring constants class.
     *
     * @return The set of instances of the constants.
     */
    public static HashSet<? extends NameValueConstant> getFields(final Class<? extends NameValueConstant> clazz)
    {
        final HashSet instances = new HashSet();

        if(clazz != null)
        {
            final Field[] fields = clazz.getFields();

            if(fields != null)
            {
                for(final Field field : fields)
                {
                    if(field != null)
                    {
                        final int fieldModifiers = field.getModifiers();

                        if(Modifier.isPublic(fieldModifiers) && Modifier.isStatic(fieldModifiers) && Modifier.isFinal(fieldModifiers))
                        {
                            try
                            {
                                final Object fieldValue = field.get(null);

                                if(fieldValue != null)
                                {
                                    if(getClassKey(clazz).equals(getClassKey(fieldValue.getClass())))
                                    {
                                        instances.add(field.get(null));
                                    }
                                }
                            }
                            catch(IllegalAccessException iae)
                            {
                                // do nothing - we don't have access to it
                            }
                        }
                    }
                }
            }
        }

        return instances;
    }

    /**
     * Two name value constant objects are equal if they are of precisely the same class, and if
     * they have the same type.  It is important that this definition not be modified or overriden.
     *
     * @param object The other object to compare with.
     *
     * @return Are they equal (same exact class and type)?
     */
    @Override
    public final boolean equals(final Object object)
    {
        boolean isEqual = false;

        if(this == object)
        {
            isEqual = true;
        }
        else
        {
            if(object != null)
            {
                if(object.getClass().equals(getClass()))
                {
                    isEqual = (_type == ((NameValueConstant) object)._type);
                }
            }
        }

        return isEqual;
    }

    /**
     * Two name value constant objects are equal if they are of precisely the same class, and if
     * they have the same type.  It is important that this definition not be modified or overriden.
     *
     * @return The hash code for <code>this</code> object.
     */
    @Override
    public final int hashCode()
    {
        return 29 * _type + getClass().hashCode();
    }

    /**
     * Creates the string representing the concrete class name along with the
     * given type(int) and name(String).
     *
     * @return The properly formatted String.
     */
    @Override
    public String toString()
    {
        final StringBuilder buffer = new StringBuilder(StringUtil.getClassName(getClass()));

        buffer.append(" - Type: ");
        buffer.append(getType());
        buffer.append(", Name: ");
        buffer.append(getName());

        return(buffer.toString());
    }
}
