package rename_to_name.ast;

import java.util.Arrays;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MethodNameLocationGetter extends ASTVisitor {
	
	public int location, length;
	public boolean found = false;
	
	/**
	 * Consists of the package and class
	 */
	String[] path;
	String declaringType;
	String methodName;
	
	@Override
	public boolean visit(MethodDeclaration node) {
		if (!node.getName().getIdentifier().equals(methodName)) return true;
		CompilationUnit cu = (CompilationUnit)node.getRoot();
		TypeDeclaration type = (TypeDeclaration)node.getParent();
		if (type == null) {
			System.err.println("type null");
		}
		if (!type.getName().getFullyQualifiedName().equals(declaringType)) return true;
		PackageDeclaration packageDeclaration = cu.getPackage();
		if (packageDeclaration == null) System.err.println("pkg");
		Name name = packageDeclaration.getName();
		if (name == null) System.err.println("name");
		String[] components = name.getFullyQualifiedName().split("[\\.]");
		if (path.length == components.length) {
			boolean equal = true;
			for (int i = 0; i < path.length; i++) {
				equal &= path[i].equals(components[i]);
			}
			if (equal) {
				location = node.getName().getStartPosition();
				length = node.getName().getLength();
				found = true;
				return false;
			}
		}
		return true;
	}
	public MethodNameLocationGetter(String[] path, String declaringType, String methodName) {
		this.path =  path;
		this.declaringType = declaringType;
		this.methodName = methodName;
	}

}
