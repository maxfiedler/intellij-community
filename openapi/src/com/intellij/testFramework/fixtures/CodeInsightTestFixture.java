/*
 * Copyright 2000-2006 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.intellij.testFramework.fixtures;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 *
 * @see IdeaTestFixtureFactory#createCodeInsightFixture(IdeaProjectTestFixture)
 *
 * @author Dmitry Avdeev
 */
public interface CodeInsightTestFixture extends IdeaTestFixture {

  @NonNls String CARET_MARKER = "<caret>";
  @NonNls String SELECTION_START_MARKER = "<selection>";
  @NonNls String SELECTION_END_MARKER = "</selection>";

  @NonNls String ERROR_MARKER = "error";
  @NonNls String WARNING_MARKER = "warning";
  @NonNls String INFORMATION_MARKER = "weak_warning";
  @NonNls String SERVER_PROBLEM_MARKER = "server_problem";
  @NonNls String INFO_MARKER = "info";
  @NonNls String END_LINE_HIGHLIGHT_MARKER = "EOLError";
  @NonNls String END_LINE_WARNING_MARKER = "EOLWarning";

  Project getProject();

  Editor getEditor();
  
  PsiFile getFile();

  void setTestDataPath(String dataPath);

  String getTempDirPath();

  /**
   * Enables inspections for highlighting tests.
   * Should be called BEFORE {@link #setUp()}.
   *
   * @param inspections inspections to be enabled in highliting tests.
   */
  void enableInspections(LocalInspectionTool... inspections);

  /**
   * Runs highliting test for the given files.
   * Checks for {@link #ERROR_MARKER} markers by default.
   *
   * @param checkWarnings enables {@link #WARNING_MARKER} support.
   * @param checkInfos enables {@link #INFO_MARKER} support.
   * @param checkWeakWarnings enables {@link #INFORMATION_MARKER} support.
   * @param filePaths the first file is tested only; the others are just copied along the first.
   *
   * @return highlighting duration in milliseconds.
   * @throws Throwable any exception thrown during highlighting.
   */
  long testHighlighting(boolean checkWarnings, boolean checkInfos, boolean checkWeakWarnings, String... filePaths) throws Throwable;

  /**
   * Runs highliting test for the given files.
   * The same as {@link #testHighlighting(boolean, boolean, boolean, String...)} with all options set.
   *
   * @param filePaths the first file is tested only; the others are just copied along with the first.
   *
   * @return highlighting duration in milliseconds
   * @throws Throwable any exception thrown during highlighting
   */
  long testHighlighting(String... filePaths) throws Throwable;

  /**
   * Finds the reference in position marked by {@link #CARET_MARKER}.
   *
   * @param filePath file to be processed.
   * @return null if no reference found.
   * @throws Throwable any exception.
   *
   * @see #getReferenceAtCaretPositionWithAssertion(String)
   */
  @Nullable
  PsiReference getReferenceAtCaretPosition(String filePath) throws Throwable;

  /**
   * Finds the reference in position marked by {@link #CARET_MARKER}.
   * Asserts that the reference exists.
   *
   * @param filePath file to be processed
   * @return founded reference
   * @throws Throwable any exception
   *
   * @see #getReferenceAtCaretPosition(String)
   */
  @NotNull
  PsiReference getReferenceAtCaretPositionWithAssertion(String filePath) throws Throwable;

  /**
   * Collects available intentions in the whole file or at caret position if {@link #CARET_MARKER} presents. 
   *
   * @param filePaths the first file is tested only; the others are just copied along with the first.
   * @return available intentions.
   * @throws Throwable any exception.
   */
  @NotNull
  Collection<IntentionAction> getAvailableIntentions(String... filePaths) throws Throwable;

  /**
   * Launches the given action. Use {@link #checkResultByFile(String)} to check the result.
   *
   * @param action the action to be launched.
   * @throws Throwable any exception.
   */
  void launchAction(IntentionAction action) throws Throwable;

  /**
   * Compares current file against the given one.
   *
   * @param expectedFile file to check against.
   * @throws Throwable any exception.
   */
  void checkResultByFile(String expectedFile) throws Throwable;

  /**
   * Compares two files.
   *
   * @param filePath file to be checked.
   * @param expectedFile file to check against.
   * @throws Throwable any exception.
   */
  void checkResultByFile(String filePath, String expectedFile) throws Throwable;

  void testCompletion(String[] filesBefore, String fileAfter) throws Throwable;

  void testCompletion(String fileBefore, String fileAfter) throws Throwable;

  /**
   * Launches renaming refactoring and checks the result.
   *
   * @param fileBefore original file path. Use {@link #CARET_MARKER} to mark the element to rename.
   * @param fileAfter result file to be checked against.
   * @param newName new name for the element.
   * @throws Throwable any exception.
   */
  void testRename(String fileBefore, String fileAfter, String newName) throws Throwable;
}
