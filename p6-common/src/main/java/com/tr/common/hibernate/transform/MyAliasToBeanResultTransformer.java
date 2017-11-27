/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.tr.common.hibernate.transform;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.hibernate.HibernateException;
import org.hibernate.property.access.internal.PropertyAccessStrategyBasicImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyChainedImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyMapImpl;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

public class MyAliasToBeanResultTransformer extends AliasedTupleSubsetResultTransformer {


    private static final long serialVersionUID = -2211766642630257367L;
    
    private final Class<?> resultClass;
    private boolean isInitialized;
    private String[] aliases;
    private Setter[] setters;

    //private Session session;
    
    public MyAliasToBeanResultTransformer(Class<?> resultClass
            //,Session session
            ) {
        if ( resultClass == null ) {
            throw new IllegalArgumentException( "resultClass cannot be null" );
        }
        isInitialized = false;
        this.resultClass = resultClass;
        //this.session = session;
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;
        try {
            if ( ! isInitialized ) {
                initialize( aliases );
            } else {
                check( aliases );
            }
            result = resultClass.newInstance();
            //this.transform(result);
            for ( int i = 0; i < aliases.length; i++ ) {
                if ( setters[i] != null ) {
                    boolean isEnum = false;
                    Class class1 = null;
                    Method m = setters[i].getMethod();
                    if(null != m){
                        Class<?>[] classes = m.getParameterTypes();
                        if(null != classes && classes.length > 0){
                            class1 = classes[0];
                            if(class1.isEnum()){
                                if(null!=tuple[i]){
                                    isEnum = true;
                                }
                            }
                        }
                    }
                    if(isEnum){
                        Enum enum1 = Enum.valueOf(class1, tuple[i].toString());
                        setters[i].set( result, enum1, null );
                    }else{
                        setters[i].set( result, tuple[i], null );
                    }
                    
                }
            }
        }catch ( InstantiationException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        }catch ( IllegalAccessException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        }
        return result;
    }
    
    private void initialize(String[] aliases) {
                PropertyAccessStrategyChainedImpl propertyAccessStrategy = new PropertyAccessStrategyChainedImpl(
                    PropertyAccessStrategyBasicImpl.INSTANCE,
                    PropertyAccessStrategyFieldImpl.INSTANCE,
                    PropertyAccessStrategyMapImpl.INSTANCE
            );
            this.aliases = new String[ aliases.length ];
            setters = new Setter[ aliases.length ];
            for ( int i = 0; i < aliases.length; i++ ) {
                String alias = aliases[ i ];
                if ( alias != null ) {
                    //TODO 字段转换  例如：user_name转换成userName
                    String[] ss = alias.split("_");
                    if(ss.length==1){
                        this.aliases[ i ] = alias;
                    }else{
                        StringBuffer buffer = new StringBuffer();
                        int j = 0;
                        for(String s:ss){
                            if( j == 0 ){
                                buffer.append(s);
                            }else{
                                String newStr = s.substring(0, 1).toUpperCase() + s.substring(1);
                                buffer.append(newStr);
                            }
                            j++;
                        }
                        this.aliases[ i ] = buffer.toString();
                    }
                    aliases[i] = this.aliases[i];
                    setters[ i ] = propertyAccessStrategy.buildPropertyAccess( resultClass, this.aliases[ i ] ).getSetter();
                    //end 
                }
            }
            isInitialized = true;
    }

    private void check(String[] aliases) {
        if ( ! Arrays.equals( aliases, this.aliases ) ) {
            throw new IllegalStateException(
                    "aliases are different from what is cached; aliases=" + Arrays.asList( aliases ) +
                            " cached=" + Arrays.asList( this.aliases ) );
        }
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        MyAliasToBeanResultTransformer that = ( MyAliasToBeanResultTransformer ) o;

        if ( ! resultClass.equals( that.resultClass ) ) {
            return false;
        }
        if ( ! Arrays.equals( aliases, that.aliases ) ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = resultClass.hashCode();
        result = 31 * result + ( aliases != null ? Arrays.hashCode( aliases ) : 0 );
        return result;
    }
}

