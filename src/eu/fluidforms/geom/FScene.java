/*
  (c) copyright
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */
 
package eu.fluidforms.geom;

import java.util.HashMap;
import java.util.Stack;

public class FScene {
	public HashMap<String, String> materials;
	public Stack<FSolid> members;
	
	public FScene(){
		materials = new HashMap<String, String>();
		members = new Stack<FSolid>();
	}
	
	public void addMember(FSolid member){
		String name = member.getName();
		if(name == null){
			name = "ASolid"+members.size();
		}
		member.setName(name);
			
		members.push(member);
	}
	
	public void addTriangle(FTriangle tri){
		FSolid defaultSolid = members.peek();
		if(defaultSolid == null){
			defaultSolid = new FSolid();
		}
		defaultSolid.addTriangle(tri);
		members.push(defaultSolid);
	}
	public FSolid getSolid(int index){
		return members.get(index);
	}
	public int solidCount(){
		return members.size();
	}
}
