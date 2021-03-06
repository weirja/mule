/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.internal;

import org.mule.extension.introspection.DescribingContext;
import org.mule.extension.introspection.declaration.DeclarationDescriptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable implementation of {@link DescribingContext}.
 * The fact that this class's attributes are immutable, doesn't mean that their inner state
 * is in fact immutable also.
 *
 * @since 3.7.0
 */
public final class ImmutableDescribingContext implements DescribingContext
{

    private final DeclarationDescriptor declarationDescriptor;
    private final Map<String, Object> customParameters = new HashMap<>();

    public ImmutableDescribingContext(DeclarationDescriptor declarationDescriptor)
    {
        this.declarationDescriptor = declarationDescriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DeclarationDescriptor getDeclarationDescriptor()
    {
        return declarationDescriptor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getCustomParameters()
    {
        return customParameters;
    }
}
