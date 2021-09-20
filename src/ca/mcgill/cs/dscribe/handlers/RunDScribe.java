package ca.mcgill.cs.dscribe.handlers;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import ca.mcgill.cs.swevo.dscribe.DScribe;
import ca.mcgill.cs.swevo.dscribe.cli.CommandLine;

/**
 * The RunDScribe class executes DScribe using the list of classes selected as input.
 * 
 * @author Alexa
 */
public class RunDScribe extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IStructuredSelection selection = getStructuredSelection(event);
    List<String> focalClassNames = getFocalClassNames(selection);
    IJavaProject javaProject = getJavaProject(selection);
    URLClassLoader classLoader = getClassLoader(javaProject);

    // Invoke DScribe and capture System.out
    var buffer = new ByteArrayOutputStream();
    System.setOut(new PrintStream(buffer));
    executeDScribe(focalClassNames, classLoader);
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    var dscribeOutput = buffer.toString();
    buffer.reset();

    // Refresh files in editor
    refreshJavaProject(javaProject);

    // Display DScribe output
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    MessageDialog.openInformation(window.getShell(), "DScribe", dscribeOutput);
    return null;
  }

  /**
   * Return the structured selection instance that the user selected to invoke the given execution
   * event
   */
  private static IStructuredSelection getStructuredSelection(ExecutionEvent event)
      throws ExecutionException {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    ISelectionService service = window.getSelectionService();
    return (IStructuredSelection) service.getSelection();
  }

  /**
   * Return a list of the focal class names corresponding to the Java files selected in the given
   * selection instance
   */
  private static List<String> getFocalClassNames(IStructuredSelection selection) {
    List<String> classNames = new ArrayList<>();
    var iterator = selection.iterator();
    while (iterator.hasNext()) {
      ICompilationUnit cu = (ICompilationUnit) iterator.next();
      classNames.add(cu.findPrimaryType().getFullyQualifiedName());
    }
    return classNames;
  }

  /**
   * Return the IJavaProject instance corresponding to the list of Java files selected in the given
   * selection instance
   */
  private static IJavaProject getJavaProject(IStructuredSelection selection) {
    ICompilationUnit firstCU = (ICompilationUnit) selection.getFirstElement();
    IType firstClass = firstCU.findPrimaryType();
    return firstClass.getJavaProject();
  }

  /**
   * Return a class loader that uses the inputed java project's class loader.
   */
  private static URLClassLoader getClassLoader(IJavaProject javaProject) {
    String[] classPathEntries;
    List<URL> urlList = new ArrayList<URL>();
    try {
      // Read the classpath entries of the java project
      classPathEntries = JavaRuntime.computeDefaultRuntimeClassPath(javaProject);
      for (String entry : classPathEntries) {
        IPath path = new Path(entry);
        var url = path.toFile().toURI().toURL();
        urlList.add(url);
      }
    } catch (CoreException | MalformedURLException e) {
      e.printStackTrace();
    }
    ClassLoader parentClassLoader = javaProject.getClass().getClassLoader();
    URL[] urls = urlList.toArray(new URL[urlList.size()]);
    return new URLClassLoader(urls, parentClassLoader);
  }

  /**
   * Refresh the given Java project
   */
  private void refreshJavaProject(IJavaProject javaProject) {
    try {
      javaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
    } catch (CoreException e) {
    }
  }


  /**
   * Run DScribe's "generateTests" and "generateDocs" commands.
   * 
   * @param args the list of focal class names for which to generate tests and docs
   * @param classLoader the ClassLoader to use to find resources
   * @return the exit code produced by DScribe
   */
  public int executeDScribe(List<String> args, ClassLoader classLoader) {
    args.add(0, "generateTests");
    var dscribe = new DScribe();
    dscribe.getContext().setClassLoader(classLoader);
    var cl = new CommandLine(dscribe);
    int exitCode = cl.execute(args.toArray(String[]::new));
    if (exitCode == 0) {
      args.set(0, "generateDocs");
      exitCode = cl.execute(args.toArray(String[]::new));
    }
    args.remove(0);
    return exitCode;
  }
}
