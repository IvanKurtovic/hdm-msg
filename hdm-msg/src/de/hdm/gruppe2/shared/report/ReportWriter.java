package de.hdm.gruppe2.shared.report;

/**
 * <p>
 * Diese Klasse wird benötigt, um auf dem Client die ihm vom Server zur
 * Verfügung gestellten <code>Report</code>-Objekte in ein menschenlesbares
 * Format zu überführen.
 * </p>
 * <p>
 * Das Zielformat kann prinzipiell beliebig sein. Methoden zum Auslesen der in
 * das Zielformat überführten Information wird den Subklassen überlassen. In
 * dieser Klasse werden die Signaturen der Methoden deklariert, die für die
 * Prozessierung der Quellinformation zuständig sind.
 * </p>
 * 
 * @author Thies
 * @author Kurtovic
 */
public abstract class ReportWriter {

  /**
   * Übersetzen eines <code>AllMessagesOfUserReport</code> in das
   * Zielformat.
   * 
   * @param r der zu übersetzende Report
   */
  public abstract void process(AllMessagesOfUserReport r);

  /**
   * Übersetzen eines <code>AllMessagesOfAllUsers</code> in das
   * Zielformat.
   * 
   * @param r der zu übersetzende Report
   */
  public abstract void process(AllMessagesOfAllUsersReport r);
  
  /**
   * Übersetzen eines <code>AllMessagesOfPeriodReport</code> in das
   * Zielformat.
   * 
   * @param r der zu übersetzende Report
   */
  public abstract void process(AllMessagesOfPeriodReport r);
  
  /**
   * Übersetzen eines <code>AllFollowersOfUserReport</code> in das
   * Zielformat.
   * 
   * @param r der zu übersetzende Report
   */
  public abstract void process(AllFollowersOfUserReport r);
  
  /**
   * Übersetzen eines <code>AllFollowersOfHashtagReport</code> in das
   * Zielformat.
   * 
   * @param r der zu übersetzende Report
   */
  public abstract void process(AllFollowersOfHashtagReport r);

}