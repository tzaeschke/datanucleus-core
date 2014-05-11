/**********************************************************************
Copyright (c) 2009 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
    ...
**********************************************************************/
package org.datanucleus.store.types.converters;

import java.util.Date;

/**
 * Class to handle the conversion between java.util.Date and a Long form.
 */
public class DateLongConverter implements TypeConverter<Date, Long>
{
    public Date toMemberType(Long value)
    {
        if (value == null)
        {
            return null;
        }

        return new java.util.Date(value);
    }

    public Long toDatastoreType(Date date)
    {
        return date != null ? date.getTime() : null;
    }
}