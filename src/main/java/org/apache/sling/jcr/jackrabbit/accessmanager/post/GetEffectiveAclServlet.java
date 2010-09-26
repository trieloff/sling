/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.sling.jcr.jackrabbit.accessmanager.post;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicy;

import org.apache.sling.jcr.base.util.AccessControlUtil;

/**
 * <p>
 * Sling GET servlet implementation for dumping the declared ACL of a resource to JSON.
 * </p>
 * <h2>Rest Service Description</h2>
 * <p>
 * Mapped to the default resourceType. Gets and Acl for a resource. Get of the form
 * &gt;resource&lt;.acl.json Provided the user has access to the ACL, they get a chunk of
 * JSON of the form.
 * </p>
 * <h4>Methods</h4>
 * <ul>
 * <li>GET</li>
 * </ul>
 * <h4>Response</h4>
 * <dl>
 * <dt>200</dt>
 * <dd>Success.</dd>
 * <dt>404</dt>
 * <dd>The resource was not found.</dd>
 * <dt>500</dt>
 * <dd>Failure. HTML explains the failure.</dd>
 * </dl>
 * <h4>Example Response</h4>
 * <code>
 * <pre>
 * {
 * &quot;principalNameA&quot;:
 *      { &quot;granted&quot; : [
 *           &quot;permission1&quot;,
 *           &quot;permission2&quot;,
 *           &quot;permission3&quot;,
 *           &quot;permission4&quot; ],
 *        &quot;denied&quot; : [
 *           &quot;permission5&quot;,
 *           &quot;permission6&quot;,
 *           &quot;permission7&quot;,
 *           &quot;permission8&quot;]
 *       },
 * &quot;principalNameB&quot;:
 *       { &quot;granted&quot; : [
 *           &quot;permission1&quot;,
 *           &quot;permission2&quot;,
 *           &quot;permission3&quot;,
 *           &quot;permission4&quot; ],
 *         &quot;denied&quot; : [
 *           &quot;permission5&quot;,
 *           &quot;permission6&quot;,
 *           &quot;permission7&quot;,
 *           &quot;permission8&quot;] },
 * &quot;principalNameC&quot;:
 *       { &quot;granted&quot; : [
 *           &quot;permission1&quot;,
 *           &quot;permission2&quot;,
 *           &quot;permission3&quot;,
 *           &quot;permission4&quot; ],
 *         &quot;denied&quot; : [
 *           &quot;permission5&quot;,
 *           &quot;permission6&quot;,
 *           &quot;permission7&quot;,
 *           &quot;permission8&quot;] }
 * }
 * </pre>
 * </code>
 *
 * @scr.component immediate="true"
 * @scr.service interface="javax.servlet.Servlet"
 * @scr.property name="sling.servlet.resourceTypes" value="sling/servlet/default"
 * @scr.property name="sling.servlet.methods" value="GET"
 * @scr.property name="sling.servlet.selectors" value="eacl"
 * @scr.property name="sling.servlet.extensions" value="json"
 */
@SuppressWarnings("serial")
public class GetEffectiveAclServlet extends AbstractGetAclServlet {

    @Override
    protected AccessControlEntry[] getAccessControlEntries(Session session, String absPath) throws RepositoryException {
        AccessControlManager accessControlManager = AccessControlUtil.getAccessControlManager(session);
        AccessControlPolicy[] policies = accessControlManager.getEffectivePolicies(absPath);
        for (AccessControlPolicy accessControlPolicy : policies) {
            if (accessControlPolicy instanceof AccessControlList) {
                AccessControlEntry[] accessControlEntries = ((AccessControlList)accessControlPolicy).getAccessControlEntries();
                return accessControlEntries;
            }
        }
        return new AccessControlEntry[0];
    }

}
