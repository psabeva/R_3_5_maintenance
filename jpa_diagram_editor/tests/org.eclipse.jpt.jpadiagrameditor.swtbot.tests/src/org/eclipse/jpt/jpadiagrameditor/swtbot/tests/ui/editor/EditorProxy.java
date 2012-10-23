package org.eclipse.jpt.jpadiagrameditor.swtbot.tests.ui.editor;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.asyncExec;
import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;
import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.mm.pictograms.FreeFormConnection;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.internal.contextbuttons.ContextButton;
import org.eclipse.graphiti.ui.internal.contextbuttons.ContextButtonPad;
import org.eclipse.graphiti.ui.internal.parts.DiagramEditPart;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentType;
import org.eclipse.jpt.jpa.ui.internal.details.JptUiDetailsMessages;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.i18n.JPAEditorMessages;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IJPAEditorFeatureProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.relations.HasReferanceRelation;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.relations.IRelation;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.relations.IRelation.RelType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.relations.IsARelation;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorConstants;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefConnectionEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.Position;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;

@SuppressWarnings("restriction")
public class EditorProxy {

	private final SWTWorkbenchBot workbenchBot;
	protected SWTGefBot bot;

	private SWTBotGefEditor jpaDiagramEditor;
	
	/**
	 * Create proxy object.
	 * 
	 * @param bot
	 */
	public EditorProxy(SWTWorkbenchBot workbenchBot, SWTGefBot bot) {
		this.workbenchBot = workbenchBot;
		this.bot = bot;
	}

	public SWTBotGefEditor openDiagramOnJPAContentNode(String name, boolean isJPA20) {
		SWTBotTree projectTree = workbenchBot.viewByTitle("Project Explorer")
				.bot().tree();
		projectTree.expandNode(name).expandNode("JPA Content").select();
		ContextMenuHelper.clickContextMenu(projectTree, "Open Diagram");

		if(isJPA20) {
			workbenchBot
				.waitUntil(
						shellIsActive(JPAEditorMessages.OpenJpaDiagramActionDelegate_jpaSupportWarningTitle),
						10000);
			SWTBotShell jpaSupportWarningDialog = workbenchBot
				.shell(JPAEditorMessages.OpenJpaDiagramActionDelegate_jpaSupportWarningTitle);
			getOkButton(jpaSupportWarningDialog).click();
		}

		SWTBotGefEditor jpaDiagramEditor = bot.gefEditor(name);
		assertFalse("Editor must not be dirty!", jpaDiagramEditor.isDirty());

		List<SWTBotGefEditPart> entities = jpaDiagramEditor.mainEditPart()
				.children();
		assertTrue("Editor must not contains any entities!", entities.isEmpty());

		return jpaDiagramEditor;
	}

	public SWTBotGefEditor openDiagramOnJPAProjectNode(String name) {
		SWTBotTree projectTree = workbenchBot.viewByTitle("Project Explorer")
				.bot().tree();
		projectTree.expandNode(name).select();
		ContextMenuHelper.clickContextMenu(projectTree, "JPA Tools",
				"Open Diagram");

		workbenchBot
				.waitUntil(
						shellIsActive(JPAEditorMessages.OpenJpaDiagramActionDelegate_jpaSupportWarningTitle),
						10000);
		SWTBotShell jpaSupportWarningDialog = workbenchBot
				.shell(JPAEditorMessages.OpenJpaDiagramActionDelegate_jpaSupportWarningTitle);
		getOkButton(jpaSupportWarningDialog).click();

		SWTBotGefEditor jpaDiagramEditor = bot.gefEditor(name);
		assertFalse("Editor must not be dirty!", jpaDiagramEditor.isDirty());
		return jpaDiagramEditor;
	}

	/**
	 * Gets the "Select Type" dialog that appears when the attribute's context
	 * menu "Refactor Attribute Type..." is selected
	 * 
	 * @param attribute
	 * @return the "Select Type" dialog
	 */
	public SWTBotShell getSelectNewAttributeTypeDialog(SWTBotGefEditPart attribute) {
		attribute.click();
		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_refactorAttributeType);

		workbenchBot
				.waitUntil(
						shellIsActive(JPAEditorMessages.SelectTypeDialog_chooseAttributeTypeDialogWindowTitle),
						10000);
		SWTBotShell changeTypeDialog = workbenchBot
				.shell(JPAEditorMessages.SelectTypeDialog_chooseAttributeTypeDialogWindowTitle);
		getNewTypeInputField(changeTypeDialog);
		return changeTypeDialog;
	}

	/**
	 * Gets the text input field of the "Select Type" dialog, which appears when
	 * the attribute's context menu "Refcator Attribute Type..." is selected
	 * 
	 * @param changeTypeDialog
	 *            - the "Select Type" dialog
	 * @return the text input field
	 */
	public SWTBotText getNewTypeInputField(SWTBotShell changeTypeDialog) {
		SWTBotText attributeType = changeTypeDialog.bot().textWithLabel(
				JPAEditorMessages.SelectTypeDialog_typeLabel);
		assertEquals("java.lang.String", attributeType.getText());
		assertTrue(getOkButton(changeTypeDialog).isEnabled());
		assertTrue(getCancelButton(changeTypeDialog).isEnabled());
		return attributeType;
	}

	/**
	 * Gets the current attribute type value
	 * 
	 * @param attributeName
	 * @param fp
	 * @return the value of the attribute's type
	 */
	public String getAttributeType(String attributeName, final IFeatureProvider fp) {
		SWTBotGefEditPart attribute = jpaDiagramEditor
				.getEditPart(attributeName);
		PictogramElement el = (PictogramElement) attribute.part().getModel();
		Object bo = fp.getBusinessObjectForPictogramElement(el);
		assertTrue("The selected element is not an attribute!",
				(bo instanceof JavaPersistentAttribute));
		String currentAttributeType = JPAEditorUtil
				.getAttributeTypeName((JavaPersistentAttribute) bo);
		return currentAttributeType;
	}

	/**
	 * Adds a new attribute to the entity and checks that: 1. The newly created
	 * attribute is selected 2. The "Other Attributes" section is visible
	 * 
	 * @param attributeName
	 *            - the name of the attribute
	 * @return the newly added attribute
	 */
	public SWTBotGefEditPart addAttributeToJPT(SWTBotGefEditPart jptType,
			String attributeName) {
		pressEntityContextButton(jptType,
				JPAEditorMessages.JPAEditorToolBehaviorProvider_createAttributeButtonlabel);

		bot.waitUntil(new ElementIsShown(jpaDiagramEditor, attributeName),
				10000);
		List<SWTBotGefEditPart> editParts = new ArrayList<SWTBotGefEditPart>();
		editParts.add(jptType);
		SWTBotGefEditPart attribute = jpaDiagramEditor
				.getEditpart(attributeName, editParts);
		assertNotNull("Atrribute is not added.", attribute);

		assertTrue("The newly added attribute must be selected.",
				jpaDiagramEditor.selectedEditParts().size() == 1);
		assertTrue("The newly added attribute must be selected.",
				jpaDiagramEditor.selectedEditParts().contains(attribute));

		assertTrue("\"Other Attributes\" section must be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes, jptType));

		return attribute;
	}

	/**
	 * Checks whether a section with the specified name is visible
	 * 
	 * @param sectionTitle
	 *            - the name of the section
	 * @return true, if the section with the specified name is visible, false
	 *         otherwise
	 */
	public boolean isSectionVisible(String sectionTitle, SWTBotGefEditPart editPart) {
		List<SWTBotGefEditPart> editParts = new ArrayList<SWTBotGefEditPart>();
		editParts.add(editPart);
		SWTBotGefEditPart section = jpaDiagramEditor.getEditpart(sectionTitle,
				editParts);
		((PictogramElement) section.part().getModel()).isVisible();
		IFigure figure = ((GraphicalEditPart) section.part()).getFigure();
		return figure.isVisible();
	}

	/**
	 * Adds an entity to the diagram
	 * 
	 * @param entityName
	 *            - the name of the entity to be added
	 * @return the added entity
	 */
	public SWTBotGefEditPart addEntityToDiagram(int x, int y, String entityName) {
		jpaDiagramEditor
				.activateTool(JPAEditorMessages.CreateJPAEntityFeature_jpaEntityFeatureName);
		jpaDiagramEditor.doubleClick(x, y);
		bot.waitUntil(new ElementIsShown(jpaDiagramEditor, entityName), 10000);

		List<SWTBotGefEditPart> entities = jpaDiagramEditor.mainEditPart()
				.children();
		assertFalse("Editor must contains at least one entity!",
				entities.isEmpty());

		SWTBotGefEditPart entity = jpaDiagramEditor.getEditPart(entityName);
		assertNotNull("Entity is not added!", entity);

		SWTBotGefEditPart idAttribute = jpaDiagramEditor.getEditPart("id");
		assertNotNull("Entity must have a primary key attribute!", idAttribute);

		assertTrue(
				"\"Primary Key\" section must be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_primaryKeysShape, entity));
		assertFalse(
				"\"Relation Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes, entity));
		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes, entity));

		return entity;
	}

	/**
	 * Adds mapped superclass to the diagram
	 * 
	 * @param entityName
	 *            - the name of the mapped superclass to be added
	 * @return the added mapped superclass
	 */
	public SWTBotGefEditPart addMappedSuperclassToDiagram(int x, int y, String entityName) {
		jpaDiagramEditor
				.activateTool(JPAEditorMessages.CreateMappedSuperclassFeature_createMappedSuperclassFeatureName);
		jpaDiagramEditor.doubleClick(x, y);
		bot.waitUntil(new ElementIsShown(jpaDiagramEditor, entityName), 10000);

		List<SWTBotGefEditPart> entities = jpaDiagramEditor.mainEditPart()
				.children();
		assertFalse("Editor must contains at least one mapped superclass!",
				entities.isEmpty());

		SWTBotGefEditPart mappedSuperclass = jpaDiagramEditor
				.getEditPart(entityName);
		assertNotNull("Mapped superclass is not added!", mappedSuperclass);

		List<SWTBotGefEditPart> parts = new ArrayList<SWTBotGefEditPart>();
		parts.add(mappedSuperclass);
		
		SWTBotGefEditPart idAttribute = jpaDiagramEditor.getEditpart("id", parts);
		assertNull("Mapped superclass must not have a primary key attribute!",
				idAttribute);

		assertFalse(
				"\"Primary Key\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_primaryKeysShape,
						mappedSuperclass));
		assertFalse(
				"\"Relation Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes,
						mappedSuperclass));
		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes,
						mappedSuperclass));

		return mappedSuperclass;
	}

	/**
	 * Adds mapped superclass to the diagram
	 * 
	 * @param entityName
	 *            - the name of the mapped superclass to be added
	 * @return the added mapped superclass
	 */
	public SWTBotGefEditPart addEmbeddableToDiagram(int x, int y, String entityName) {
		jpaDiagramEditor.activateTool(JPAEditorMessages.CreateEmbeddableFeature_EmbeddableFeatureName);
		jpaDiagramEditor.doubleClick(x, y);
		bot.waitUntil(new ElementIsShown(jpaDiagramEditor, entityName), 10000);

		List<SWTBotGefEditPart> entities = jpaDiagramEditor.mainEditPart()
				.children();
		assertFalse("Editor must contains at least one embeddable!",
				entities.isEmpty());

		SWTBotGefEditPart embeddable = jpaDiagramEditor.getEditPart(entityName);
		assertNotNull("Embeddable is not added!", embeddable);

		List<SWTBotGefEditPart> editParts = new ArrayList<SWTBotGefEditPart>();
		editParts.add(embeddable);
		SWTBotGefEditPart idAttribute = jpaDiagramEditor.getEditpart("id",
				editParts);
		assertNull("Embeddablemust not have a primary key attribute!",
				idAttribute);

		assertFalse(
				"\"Primary Key\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_primaryKeysShape,
						embeddable));
		assertFalse(
				"\"Relation Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes,
						embeddable));
		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes,
						embeddable));

		return embeddable;
	}

	/**
	 * Pressing the "Yes" button of the "Confirm Delete" question dialog.
	 */
	public void confirmDelete() {
		SWTBotShell shell = getDeleteEntityDialog();
		shell.bot().button("Yes").click();
	}

	/**
	 * Pressing the "No" button of the "Confirm Delete" question dialog.
	 */
	public void denyDelete() {
		SWTBotShell shell = getDeleteEntityDialog();
		shell.bot().button("No").click();
	}

	/**
	 * Gets the dialog that appears after the "Delete" context button/menu is
	 * pressed.
	 * 
	 * @return the question dialog, asking whether to the delete the selected
	 *         entity
	 */
	public SWTBotShell getDeleteEntityDialog() {
		workbenchBot.waitUntil(
				shellIsActive(JPAEditorMessages.DeleteFeature_deleteConfirm),
				10000);
		SWTBotShell shell = workbenchBot
				.shell(JPAEditorMessages.DeleteFeature_deleteConfirm);
		return shell;
	}

	/**
	 * Gets the dialog that appears after the
	 * "Refactor Entity Class -> Rename..." context menu is pressed.
	 * 
	 * @return the "Rename Compilation Unit" dialog
	 */
	public SWTBotShell getRenameEntityDialog() {
		workbenchBot.waitUntil(shellIsActive("Rename Compilation Unit"), 10000);
		SWTBotShell shell = workbenchBot.shell("Rename Compilation Unit");
		assertFalse(getFinishButton(shell).isEnabled());
		assertTrue(getCancelButton(shell).isEnabled());
		return shell;
	}

	/**
	 * Gets the dialog that appears after the "Refactor Entity Class -> Move..."
	 * context menu is pressed.
	 * 
	 * @return the "Move" dialog
	 */
	public SWTBotShell getMoveEntityDialog() {
		workbenchBot.waitUntil(shellIsActive("Move"), 10000);
		SWTBotShell shell = workbenchBot.shell("Move");
		assertFalse(getOkButton(shell).isEnabled());
		assertTrue(getCancelButton(shell).isEnabled());
		return shell;
	}

	/**
	 * Gets the dialog that appears after the
	 * "Remove All Entities from Diagram -> ...and Save/Discard Changes" context
	 * menu is pressed. Press the OK button.
	 */
	public void confirmRemoveEntitiesFromDiagramDialog() {
		workbenchBot.waitUntil(
			shellIsActive(JPAEditorMessages.JPAEditorToolBehaviorProvider_removeAllEntitiesMenu), 10000);
		SWTBotShell shell = workbenchBot
				.shell(JPAEditorMessages.JPAEditorToolBehaviorProvider_removeAllEntitiesMenu);
		assertTrue("Ok button is disabled", getOkButton(shell).isEnabled());
		assertTrue(getCancelButton(shell).isEnabled());
		getOkButton(shell).click();
	}

	/**
	 * Deletes an entity with the specified name using the context button.
	 * 
	 * @param entityName
	 *            - the name of the entity to be deleted
	 */
	public void deleteDiagramElements() {

		jpaDiagramEditor.save();

		List<SWTBotGefEditPart> entitiesInDiagram = jpaDiagramEditor
				.mainEditPart().children();
		assertFalse("Diagram must contain at least one entity!",
				entitiesInDiagram.isEmpty());

		for (int i = 0; i < entitiesInDiagram.size(); i++) {
			SWTBotGefEditPart editPart = entitiesInDiagram.get(i);
			assertNotNull(editPart);
			JavaPersistentType type = getJPTObjectForGefElement(editPart);
			if (type != null) {
				editPart.select();
				jpaDiagramEditor.clickContextMenu("Delete");
				confirmDelete();
			}
		}

		waitASecond();
		entitiesInDiagram = jpaDiagramEditor.mainEditPart().children();
		assertTrue("Diagram must be empty!", entitiesInDiagram.isEmpty());
		assertTrue("Editor must be dirty!", jpaDiagramEditor.isDirty());
	}

	public void deleteAttributeInJPT(SWTBotGefEditPart jpt, String attributeName) {
		
		waitASecond();
		jpaDiagramEditor.activateDefaultTool();
		
		List<SWTBotGefEditPart> jptParts = new ArrayList<SWTBotGefEditPart>();
		jptParts.add(jpt);
		SWTBotGefEditPart attribute = jpaDiagramEditor.getEditpart(
				attributeName, jptParts);
		attribute.select();
		attribute.click();
		jpaDiagramEditor.clickContextMenu("Delete");
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor, jpt, attributeName),
				10000);
	}

	/**
	 * Press some of the entity's context buttons
	 * 
	 * @param jpaDiagramEditor
	 * @param contextButtonName
	 *            - the name of the button to be pressed
	 */
	public void pressEntityContextButton(SWTBotGefEditPart part, String contextButtonName) {
		pressContextButton(part, contextButtonName);
	}

	/**
	 * Press the "Delete Attribute" attribute's context button
	 * 
	 */
	public void pressAttributeDeleteContextButton(SWTBotGefEditPart part) {
		pressContextButton(part,
				JPAEditorMessages.JPAEditorToolBehaviorProvider_deleteAttributeButtonlabel);
	}

	/**
	 * Assert that the context button pad is shown, when the mouse is placed
	 * over the entity and press the the desired button
	 * 
	 * @param contextButtonName
	 *            - the name of the button to be pressed.
	 */
	private void pressContextButton(SWTBotGefEditPart part, String contextButtonName) {
		jpaDiagramEditor.click(0, 0);
		jpaDiagramEditor.click(part);

		ContextButtonPad pad = findContextButtonPad();
		assertNotNull(pad);
		for (final Object button : pad.getChildren()) {
			if (((ContextButton) button).getEntry().getText()
					.equals(contextButtonName)) {
				asyncExec(new VoidResult() {
					public void run() {
						((ContextButton) button).doClick();

					}
				});
			}
		}
	}

	/**
	 * Place the mouse over the entity to show the context button pad.
	 * 
	 */
	public void moveMouse(final int x, final int y) {
		syncExec(new VoidResult() {
			public void run() {
				Robot r;
				try {
					r = new Robot();
					Point p = getOrigin();
					r.mouseMove(p.x + x, p.y + y);
				} catch (AWTException e) {
					fail(e.getMessage());
				}
			}
		});
	}

	/**
	 * Gets the context button pad, after placing the mouse over the entity
	 * 
	 * @return the entity's context button pad
	 */
	@SuppressWarnings("restriction")
	private ContextButtonPad findContextButtonPad() {
		SWTBotGefEditPart rootEditPart = jpaDiagramEditor.rootEditPart();
		IFigure feedbackLayer = ((ScalableFreeformRootEditPart) rootEditPart
				.part()).getLayer(LayerConstants.HANDLE_LAYER);
		ContextButtonPad cbp = null;
		for (Object obj : feedbackLayer.getChildren()) {
			if (obj instanceof ContextButtonPad) {
				cbp = (ContextButtonPad) obj;
				break;
			}
		}
		return cbp;
	}

	private FigureCanvas getCanvas() {
		IEditorReference reference = jpaDiagramEditor.getReference();
		final IEditorPart editor = reference.getEditor(true);
		GraphicalViewer graphicalViewer = (GraphicalViewer) editor
				.getAdapter(GraphicalViewer.class);
		final Control control = graphicalViewer.getControl();
		if (control instanceof FigureCanvas) {
			FigureCanvas c = (FigureCanvas) control;
			return c;
		}
		return null;
	}

	private Point getOrigin() {
		Canvas c = getCanvas();
		Point p = c.toDisplay(0, 0);
		return p;
	}

	/**
	 * Get the error message that appears in the "Select Type" dialog
	 * 
	 * @param dialog
	 * @return the error message
	 */
	public SWTBotText getDialogErroMessage(SWTBotShell dialog) {
		return dialog.bot().text(1);
	}

	/**
	 * Gets the "OK" button of a dialog
	 * 
	 * @param dialog
	 * @return the "OK" button
	 */
	public SWTBotButton getOkButton(SWTBotShell dialog) {
		return dialog.bot().button(IDialogConstants.OK_LABEL);
	}

	/**
	 * Gets the "Cancel" button of a dialog
	 * 
	 * @param dialog
	 * @return the "Cancel" button
	 */
	public SWTBotButton getCancelButton(SWTBotShell dialog) {
		return dialog.bot().button(IDialogConstants.CANCEL_LABEL);
	}

	/**
	 * Gets the "Finish" button of a dialog
	 * 
	 * @param dialog
	 * @return the "Finish" button
	 */
	public SWTBotButton getFinishButton(SWTBotShell dialog) {
		return dialog.bot().button("Finish");
	}

	/**
	 * Find the IRelation object for the given GEF Connection
	 * 
	 * @param gefConn
	 * @return the IRelation object for the given GEF Connection
	 */
	@SuppressWarnings("restriction")
	public IRelation getConnection(SWTBotGefConnectionEditPart gefConn) {
		IFeatureProvider fp = ((DiagramEditPart) jpaDiagramEditor
				.mainEditPart().part()).getFeatureProvider();
		FreeFormConnection conn = (FreeFormConnection) gefConn.part()
				.getModel();
		Object ob = fp.getBusinessObjectForPictogramElement(conn);
		if (ob instanceof IRelation) {
			return (IRelation) ob;
		}

		return null;
	}

	/**
	 * Find the IRelation object for the given GEF Connection
	 * 
	 * @param gefConn
	 * @return the IRelation object for the given GEF Connection
	 */
	@SuppressWarnings("restriction")
	public HasReferanceRelation getHasReferenceConnection(SWTBotGefConnectionEditPart gefConn) {
		IFeatureProvider fp = ((DiagramEditPart) jpaDiagramEditor
				.mainEditPart().part()).getFeatureProvider();
		FreeFormConnection conn = (FreeFormConnection) gefConn.part()
				.getModel();
		Object ob = fp.getBusinessObjectForPictogramElement(conn);
		if (ob instanceof HasReferanceRelation) {
			return (HasReferanceRelation) ob;
		}

		return null;
	}

	/**
	 * Gets the business object (JavaPersistentType) for the given GEF element
	 * 
	 * @param element
	 * @return the java persistent type for the given element, null if the
	 *         selected element is not an entity
	 */
	public JavaPersistentType getJPTObjectForGefElement(SWTBotGefEditPart element) {
		final IFeatureProvider fp = ((DiagramEditPart) jpaDiagramEditor
				.mainEditPart().part()).getFeatureProvider();
		PictogramElement el = (PictogramElement) element.part().getModel();
		Object bo = fp.getBusinessObjectForPictogramElement(el);
		if (bo instanceof JavaPersistentType) {
			return (JavaPersistentType) bo;
		}
		return null;
	}

	public JavaPersistentAttribute getJPAObjectForGefElement(SWTBotGefEditPart element) {
		final IFeatureProvider fp = ((DiagramEditPart) jpaDiagramEditor
				.mainEditPart().part()).getFeatureProvider();
		PictogramElement el = (PictogramElement) element.part().getModel();
		Object bo = fp.getBusinessObjectForPictogramElement(el);
		if (bo instanceof JavaPersistentAttribute) {
			return (JavaPersistentAttribute) bo;
		}
		return null;
	}

	/**
	 * Gets the existing isARelation
	 * 
	 * @return the existing isArelation if exists, null otherwise
	 */
	@SuppressWarnings("restriction")
	public IsARelation getIsARelationship() {
		IJPAEditorFeatureProvider fp = (IJPAEditorFeatureProvider) ((DiagramEditPart) jpaDiagramEditor
				.mainEditPart().part()).getFeatureProvider();
		Set<IsARelation> isARelationships = fp.getAllExistingIsARelations();
		assertFalse(isARelationships.isEmpty());
		assertEquals(1, isARelationships.size());
		IsARelation relation = isARelationships.iterator().next();
		return relation;
	}

	/**
	 * CHecks whether the Entity contains unsaved changes.
	 * 
	 * @param element
	 * @return true if the entity contains unsaved changes, false otherwise
	 */
	@SuppressWarnings("restriction")
	public boolean isJPTDirty(SWTBotGefEditPart element) {
		final IFeatureProvider fp = ((DiagramEditPart) jpaDiagramEditor
				.mainEditPart().part()).getFeatureProvider();
		PictogramElement el = (PictogramElement) element.part().getModel();
		Object bo = fp.getBusinessObjectForPictogramElement(el);
		IResource res = null;
		if (bo instanceof JavaPersistentAttribute) {
			res = ((JavaPersistentAttribute) bo).getResource();
		} else if (bo instanceof JavaPersistentType) {
			res = ((JavaPersistentType) bo).getResource();
		}

		if (res != null) {
			ICompilationUnit unit = JPAEditorUtil
					.getCompilationUnit((IFile) res);
			try {
				return unit.hasUnsavedChanges();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * Select the bidirectional relation and call its "Delete" context menu. On
	 * the confirmation dialog press "No" and assert that the connection and the
	 * relative relation attributes still exist and the "Relation Attributes"
	 * sections of the entities' are visible.
	 * 
	 * @param entity1
	 * @param entity2
	 * @param connection
	 * @param ownerAttributeName
	 * @param inverseAttributeName
	 */
	public void assertBiDirRelationIsNotDeleted(SWTBotGefEditPart entity1,
			SWTBotGefEditPart entity2, SWTBotGefConnectionEditPart connection,
			String ownerAttributeName, String inverseAttributeName) {
		connection.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		denyDelete();
		assertFalse(entity1.sourceConnections().isEmpty());
		assertFalse(entity2.targetConnections().isEmpty());
		connection = entity1.sourceConnections().get(0);
		assertNotNull("Attribute must not be deleted!", connection);
		assertNotNull(jpaDiagramEditor.getEditPart(ownerAttributeName));
		assertNotNull(jpaDiagramEditor.getEditPart(inverseAttributeName));
		assertTrue(
				"\"Relation Attributes\" section of the owner entity must be visible!",
				isSectionVisible(entity1,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
		assertTrue(
				"\"Relation Attributes\" section of the inverse entity must be visible!",
				isSectionVisible(entity2,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	/**
	 * Select the bidirectional self relation and call its "Delete" context
	 * menu. On the confirmation dialog press "No" and assert that the
	 * connection and the relative relation attributes still exist and the
	 * "Relation Attributes" sections of the entities' are visible.
	 * 
	 * @param entity1
	 * @param connection
	 * @param ownerAttributeName
	 * @param inverseAttributeName
	 */
	public void assertSelfBiDirRelationIsNotDeleted(SWTBotGefEditPart entity1,
			SWTBotGefConnectionEditPart connection, String ownerAttributeName,
			String inverseAttributeName) {
		connection.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		denyDelete();
		assertFalse(entity1.sourceConnections().isEmpty());
		assertFalse(entity1.targetConnections().isEmpty());
		connection = entity1.sourceConnections().get(0);
		assertNotNull("Attribute must not be deleted!", connection);
		assertNotNull(jpaDiagramEditor.getEditPart(ownerAttributeName));
		assertNotNull(jpaDiagramEditor.getEditPart(inverseAttributeName));
		assertTrue(
				"\"Relation Attributes\" section of the owner entity must be visible!",
				isSectionVisible(entity1,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	/**
	 * Select the unidirectional relation and call its "Delete" context menu. On
	 * the confirmation dialog press "No" and assert that the connection and the
	 * relative relation attributes still exist and the "Relation Attributes"
	 * sections of the entities' are visible.
	 * 
	 * @param entity1
	 * @param entity2
	 * @param connection
	 * @param ownerAttributeName
	 * @param inverseAttributeName
	 */
	public void assertUniDirRelationIsNotDeleted(SWTBotGefEditPart entity1,
			SWTBotGefEditPart entity2, SWTBotGefConnectionEditPart connection,
			String attributeName) {
		connection.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		denyDelete();
		assertFalse(entity1.sourceConnections().isEmpty());
		assertFalse(entity2.targetConnections().isEmpty());
		connection = entity1.sourceConnections().get(0);
		assertNotNull("Attribute must not be deleted!", connection);
		assertNotNull(jpaDiagramEditor.getEditPart(attributeName));
		assertTrue(
				"\"Relation Attributes\" section of the owner entity must be visible!",
				isSectionVisible(entity1,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
		assertFalse(
				"\"Relation Attributes\" section of the inverse entity must not be visible!",
				isSectionVisible(entity2,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	/**
	 * Select the unidirectional self relation and call its "Delete" context
	 * menu. On the confirmation dialog press "No" and assert that the
	 * connection and the relative relation attributes still exist and the
	 * "Relation Attributes" sections of the entities' are visible.
	 * 
	 * @param entity1
	 * @param connection
	 * @param ownerAttributeName
	 * @param inverseAttributeName
	 */
	public void assertSelfUniDirRelationIsNotDeleted(SWTBotGefEditPart entity1,
			SWTBotGefConnectionEditPart connection, String attributeName) {
		connection.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		denyDelete();
		assertFalse(entity1.sourceConnections().isEmpty());
		assertFalse(entity1.targetConnections().isEmpty());
		connection = entity1.sourceConnections().get(0);
		assertNotNull("Attribute must not be deleted!", connection);
		assertNotNull(jpaDiagramEditor.getEditPart(attributeName));
		assertTrue(
				"\"Relation Attributes\" section of the owner entity must be visible!",
				isSectionVisible(entity1,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	/**
	 * Select the bidirectional relation and call its "Delete" context menu. On
	 * the confirmation dialog press "Yes" and assert that the connection and
	 * the relative relation attributes do not exist anymore and the
	 * "Relation Attributes" sections of the entities' are not visible.
	 * 
	 * @param entity1
	 * @param entity2
	 * @param connection
	 * @param ownerAttributeName
	 * @param inverseAttributeName
	 */
	public void assertBiDirRelationIsDeleted(SWTBotGefEditPart entity1, SWTBotGefEditPart entity2,
			SWTBotGefConnectionEditPart connection, String ownerAttributeName,
			String inverseAttributeName) {
		connection.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor,
				ownerAttributeName), 10000);
		assertTrue(entity1.sourceConnections().isEmpty());
		assertTrue(entity2.targetConnections().isEmpty());
		assertNull(jpaDiagramEditor.getEditPart(ownerAttributeName));
		assertNull(jpaDiagramEditor.getEditPart(inverseAttributeName));
				
		assertFalse(
				"\"Relation Attributes\" section of the owner entity must not be visible!",
				isSectionVisible(entity1,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
		assertFalse(
				"\"Relation Attributes\" section of the inverse entity must not be visible!",
				isSectionVisible(entity2,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	/**
	 * Select the bidirectional self relation and call its "Delete" context
	 * menu. On the confirmation dialog press "Yes" and assert that the
	 * connection and the relative relation attributes do not exist anymore and
	 * the "Relation Attributes" sections of the entities' are not visible.
	 * 
	 * @param entity1
	 * @param connection
	 * @param ownerAttributeName
	 * @param inverseAttributeName
	 */
	public void assertSelfBiDirRelationIsDeleted(SWTBotGefEditPart entity1,
			SWTBotGefConnectionEditPart connection, String ownerAttributeName,
			String inverseAttributeName) {
		connection.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor,
				ownerAttributeName), 10000);
		assertTrue(entity1.sourceConnections().isEmpty());
		assertTrue(entity1.targetConnections().isEmpty());
		assertNull(jpaDiagramEditor.getEditPart(ownerAttributeName));
		assertNull(jpaDiagramEditor.getEditPart(inverseAttributeName));
		assertFalse(
				"\"Relation Attributes\" section of the owner entity must not be visible!",
				isSectionVisible(entity1,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	/**
	 * Select the unidirectional relation and call its "Delete" context menu. On
	 * the confirmation dialog press "Yes" and assert that the connection and
	 * the relative relation attributes do not exist anymore and the
	 * "Relation Attributes" sections of the entities' are not visible.
	 * 
	 * @param entity1
	 * @param entity2
	 * @param connection
	 * @param ownerAttributeName
	 * @param inverseAttributeName
	 */
	public void assertUniDirRelationIsDeleted(SWTBotGefEditPart entity1, SWTBotGefEditPart entity2,
			SWTBotGefConnectionEditPart connection, String attributeName) {
		connection.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor, attributeName),
				10000);
		assertTrue(entity1.sourceConnections().isEmpty());
		assertTrue(entity2.targetConnections().isEmpty());
		assertNull(jpaDiagramEditor.getEditPart(attributeName));
		assertFalse(
				"\"Relation Attributes\" section of the owner entity must not be visible!",
				isSectionVisible(entity1,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
		assertFalse(
				"\"Relation Attributes\" section of the inverse entity must not be visible!",
				isSectionVisible(entity2,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	/**
	 * Select the unidirectional self relation and call its "Delete" context
	 * menu. On the confirmation dialog press "Yes" and assert that the
	 * connection and the relative relation attributes do not exist anymore and
	 * the "Relation Attributes" sections of the entities' are not visible.
	 * 
	 * @param entity1
	 * @param connection
	 * @param ownerAttributeName
	 * @param inverseAttributeName
	 */
	public void assertSelfUniDirRelationIsDeleted(SWTBotGefEditPart entity1,
			SWTBotGefConnectionEditPart connection, String attributeName) {
		connection.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor, attributeName),
				10000);
		assertTrue(entity1.sourceConnections().isEmpty());
		assertTrue(entity1.targetConnections().isEmpty());
		assertNull(jpaDiagramEditor.getEditPart(attributeName));
		assertFalse(
				"\"Relation Attributes\" section of the owner entity must not be visible!",
				isSectionVisible(entity1,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	/**
	 * Assert that there is exactly one GEF element representing the
	 * relationship
	 * 
	 * @param entity1
	 * @param entity2
	 */
	public void assertConnectionIsCreated(SWTBotGefEditPart entity1, SWTBotGefEditPart entity2,
			boolean isBiDIr) {
		// assert that there is exactly one relationship, which start from
		// entity1
		// and that there is no relationship which starts from entity2
		assertFalse(entity1.sourceConnections().isEmpty());
		assertEquals(1, entity1.sourceConnections().size());
		assertTrue(entity2.sourceConnections().isEmpty());

		// assert that there is exactly one relationship which ends in entity2
		// and that there is no relationship which end in entity1.
		assertFalse(entity2.targetConnections().isEmpty());
		assertEquals(1, entity2.targetConnections().size());
//		assertTrue(entity1.targetConnections().isEmpty());

		assertTrue(
				"\"Relation Attributes\" section of the owner entity must be visible!",
				isSectionVisible(entity1,
						JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
		if (isBiDIr) {
			assertTrue(
					"\"Relation Attributes\" section of the inverse entity must be visible!",
					isSectionVisible(entity2,
							JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
		} else {
			assertFalse(
					"\"Relation Attributes\" section of the inverse entity must not be visible!",
					isSectionVisible(entity2,
							JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
		}
	}

	public void assertIsARelationExists(SWTBotGefEditPart entity1, SWTBotGefEditPart entity2) {
		// assert that there is exactly one relationship, which start from
		// entity2 and that there is no relationship which starts from entity2
		assertFalse(entity2.sourceConnections().isEmpty());
		assertEquals(1, entity2.sourceConnections().size());
		assertTrue(entity1.sourceConnections().isEmpty());

		// assert that there is exactly one relationship which ends in entity1
		// and that there is no relationship which end in entity2.
		assertFalse(entity1.targetConnections().isEmpty());
		assertEquals(1, entity1.targetConnections().size());
		assertTrue(entity2.targetConnections().isEmpty());
	}

	/**
	 * Assert that there is exactly one GEF element representing the self
	 * relationship
	 * 
	 * @param entity1
	 */
	public void assertSelfConnectionIsCreated(SWTBotGefEditPart entity1) {
		// assert that there is exactly one relationship, which start from
		// entity1
		// and ends in entity2
		assertFalse(entity1.sourceConnections().isEmpty());
		assertEquals(1, entity1.sourceConnections().size());

		assertFalse(entity1.targetConnections().isEmpty());
		assertEquals(1, entity1.targetConnections().size());

		assertTrue(isSectionVisible(entity1, JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	/**
	 * Assert that the owner relationship attribute exists
	 * 
	 * @param rel
	 * @return the name of the owner relationship attribute
	 */
	public String testOwnerRelationAttributeProperties(IRelation rel) {
		JavaPersistentAttribute ownerAttr = rel.getOwnerAnnotatedAttribute();
		String attributeName = rel.getOwnerAttributeName();
		assertNotNull(ownerAttr);
		assertNotNull(jpaDiagramEditor.getEditPart(attributeName));

		return attributeName;
	}

	/**
	 * Assert that the embedded attribute exists
	 * 
	 * @param rel
	 * @return the name of the embedding attribute
	 */
	public String testEmbeddedAttributeProperties(HasReferanceRelation rel,
			String attributeMapping) {
		JavaPersistentAttribute embeddedAttr = rel
				.getEmbeddedAnnotatedAttribute();
		String attributeName = embeddedAttr.getName();
		assertNotNull(embeddedAttr);
		assertNotNull(jpaDiagramEditor.getEditPart(attributeName));
		assertEquals("The attribute must be embedded attribute.",
				embeddedAttr.getMappingKey(), attributeMapping);

		return attributeName;
	}

	/**
	 * Assert that the inverse relationship attribute exists.
	 * 
	 * @param rel
	 * @return the name of the inverse relationship attribute
	 */
	public String testInverseRelationAttributeProperties(IRelation rel) {
		JavaPersistentAttribute inverseAttr = rel
				.getInverseAnnotatedAttribute();
		String inverseAttributeName = rel.getInverseAttributeName();
		assertNotNull(inverseAttr);
		assertNotNull(jpaDiagramEditor.getEditPart(inverseAttributeName));
		return inverseAttributeName;
	}

	/**
	 * Checks whether a section of a particular entity is visible
	 * 
	 * @param editPart
	 *            - the particular entity
	 * @param sectionTitle
	 *            - the title of the section to be checked
	 * @return true, if the sections is visible, false otherwise
	 */
	@SuppressWarnings("deprecation")
	public boolean isSectionVisible(SWTBotGefEditPart editPart, String sectionTitle) {
		List<SWTBotGefEditPart> children = editPart.children();
		SWTBotGefEditPart section = jpaDiagramEditor.getEditpart(sectionTitle,
				children);
		((PictogramElement) section.part().getModel()).isVisible();
		IFigure figure = ((GraphicalEditPart) section.part()).getFigure();
		return figure.isVisible() || figure.isShowing();
	}

	public SWTBotGefEditPart getSectionInJPT(SWTBotGefEditPart editPart, String sectionTitle) {
		List<SWTBotGefEditPart> children = editPart.children();
		SWTBotGefEditPart section = jpaDiagramEditor.getEditpart(sectionTitle,
				children);
		return section;
	}

	/**
	 * Change the mapping of the type or attribute.
	 * 
	 * @param newMappingType
	 */
	public void changeMappingtype(String newMappingType) {
		workbenchBot.waitUntil(shellIsActive("Mapping Type Selection"), 10000);
		SWTBotShell mappingTypeShell = workbenchBot
				.shell("Mapping Type Selection");
		mappingTypeShell.bot().table().getTableItem(newMappingType).select();
		getOkButton(mappingTypeShell).click();
	}

	/**
	 * Click on the mapping type link in the JPA Details view
	 * 
	 * @param styledText
	 * @param position
	 */
	public void clickOnStyledText(final SWTBotStyledText styledText,
			int position) {
		styledText.navigateTo(new Position(0, position));
		asyncExec(new VoidResult() {
			public void run() {
				styledText.widget.notifyListeners(SWT.MouseDown, new Event());
				styledText.widget.notifyListeners(SWT.MouseUp, new Event());
			}
		});
	}

	public void waitASecond() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Assert that the relation attribute is correctly mapped in the JPA Details
	 * view
	 * 
	 * @param attributeName
	 * @param relationAttributeMapping
	 *            - the expected attribute mapping
	 */
	public void assertAttributeIsCorretlyMapped(String attributeName,
			String relationAttributeMapping) {

		// assert that the JPA Details view is opened
		SWTBotView jpaDetailsView = workbenchBot.viewByTitle("JPA Details");
		jpaDetailsView.setFocus();
		assertTrue("JPA Details view must be opened!",
				jpaDetailsView.isActive());

		SWTBotGefEditPart attribute = jpaDiagramEditor
				.getEditPart(attributeName);
		attribute.select();
		attribute.click();

		// assert that the default entity's attribute is mapped as primary key
		SWTBot jpaDetailsBot = jpaDetailsView.bot();
		SWTBotStyledText styledText = jpaDetailsBot.styledText();
		assertEquals("Attribute '" + attributeName + "' is mapped as "
				+ relationAttributeMapping + ".", styledText.getText());
	}

	/**
	 * Assert that the type is correctly mapped in the JPA Details view
	 * 
	 * @param typeName
	 * @param typeMapping
	 *            - the expected type mapping
	 */
	public void assertTypeIsCorretlyMapped(String typeName, String typeMapping) {
		workbenchBot.viewByTitle("JPA Details").close();
		
		SWTBotGefEditPart type = jpaDiagramEditor.getEditPart(typeName);
		type.click();
		
		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_openJPADetailsView);
		// assert that the JPA Details view is opened
		SWTBotView jpaDetailsView = workbenchBot.viewByTitle("JPA Details");
		assertTrue("JPA Details view must be opened!",
				jpaDetailsView.isActive());

		// assert that the default entity's attribute is mapped as the given
		// mapping key
		SWTBot jpaDetailsBot = jpaDetailsView.bot();
		SWTBotStyledText styledText = jpaDetailsBot.styledText();
		assertEquals("Type '" + typeName + "' is mapped as " + typeMapping
				+ ".", styledText.getText());
	}

	public void deleteJPTViaButton(SWTBotGefEditPart jptType) {

		String jptName = getJPTObjectForGefElement(jptType)
				.getSimpleName();

		pressEntityContextButton(jptType,
				JPAEditorMessages.JPAEditorToolBehaviorProvider_deleteEntityFromModelButtonLabel);
		denyDelete();

		jptType = jpaDiagramEditor.getEditPart(jptName);
		assertNotNull("Entity is deleted!", jptType);

		pressEntityContextButton(jptType,
				JPAEditorMessages.JPAEditorToolBehaviorProvider_deleteEntityFromModelButtonLabel);
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor, jptName), 10000);
		jptType = jpaDiagramEditor.getEditPart(jptName);
		assertNull("Entity is not deleted!", jptType);
	}

	public void deleteJPTViaMenu(SWTBotGefEditPart jptType) {

		String jptName = getJPTObjectForGefElement(jptType)
				.getSimpleName();

		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_deleteEntityFromModelButtonLabel);
		denyDelete();

		jptType = jpaDiagramEditor.getEditPart(jptName);
		assertNotNull("Entity is deleted!", jptType);

		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_deleteEntityFromModelButtonLabel);
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor, jptName), 10000);
		jptType = jpaDiagramEditor.getEditPart(jptName);
		assertNull("Entity is not deleted!", jptType);
	}

	public void removeAttributeViaButton(SWTBotGefEditPart jptType, String attributeName) {

		assertFalse(
				"\"Other Attributes\" section must not be visible!",

				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes,
						jptType));

		SWTBotGefEditPart attribute = addAttributeToJPT(jptType, attributeName);
		assertTrue("Editor must be dirty", jpaDiagramEditor.isDirty());
		jpaDiagramEditor.save();

		pressAttributeDeleteContextButton(attribute);
		denyDelete();
		attribute = jpaDiagramEditor.getEditPart(attributeName);
		assertNotNull("Attribute must not be deleted!", attribute);

		pressAttributeDeleteContextButton(attribute);
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor, attributeName),
				20000);
		attribute = jpaDiagramEditor.getEditPart(attributeName);
		assertNull("Attribute must be deleted!", attribute);

		jpaDiagramEditor.save();
		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes,
						jptType));

	}

	public void removeAttributeViaMenu(SWTBotGefEditPart jptType, String attributeName) {
		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes,
						jptType));

		SWTBotGefEditPart attribute = addAttributeToJPT(jptType, attributeName);
		assertTrue("Editor must be dirty", jpaDiagramEditor.isDirty());
		jpaDiagramEditor.save();

		attribute.click();
		jpaDiagramEditor.clickContextMenu("Delete");
		denyDelete();
		attribute = jpaDiagramEditor.getEditPart(attributeName);
		assertNotNull("Attribute must not be deleted!", attribute);

		attribute.click();
		jpaDiagramEditor.clickContextMenu("Delete");
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor, attributeName),
				20000);
		attribute = jpaDiagramEditor.getEditPart(attributeName);
		assertNull("Attribute must be deleted!", attribute);

		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes,
						jptType));
	}

	public void directEditAttribute(SWTBotGefEditPart jptType, String attributeName) {

		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes,
						jptType));

		SWTBotGefEditPart attribute = addAttributeToJPT(jptType, attributeName);
		assertNotNull("The attribute must not be renamed!", attribute);

		jpaDiagramEditor.directEditType("newAttrName");
		SWTBotGefEditPart oldAttribute = jpaDiagramEditor
				.getEditPart(attributeName);
		SWTBotGefEditPart newAttribute = jpaDiagramEditor
				.getEditPart("newAttrName");
		assertNotNull("The attribute must be renamed!", newAttribute);
		assertNull("The attribute must be renamed!", oldAttribute);
	}

	public void collapseExpandJPTViaButton(SWTBotGefEditPart jptType) {

		int heigth = ((PictogramElement) jptType.part().getModel())
				.getGraphicsAlgorithm().getHeight();

		pressEntityContextButton(jptType, "Collapse");
		waitASecond();

		int newHeight = ((PictogramElement) jptType.part().getModel())
				.getGraphicsAlgorithm().getHeight();
		assertEquals("Entity must be collapsed!",
				JPAEditorConstants.ENTITY_MIN_HEIGHT, newHeight);
		assertTrue(newHeight < heigth);

		pressEntityContextButton(jptType, "Expand");
		waitASecond();
		
		newHeight = ((PictogramElement) jptType.part().getModel())
				.getGraphicsAlgorithm().getHeight();
		assertEquals("Entity must be expanded!", heigth, newHeight);
		assertTrue(newHeight > JPAEditorConstants.ENTITY_MIN_HEIGHT);
	}

	public void collapseExpandJPTViaMenu(SWTBotGefEditPart jptType) {

		int heigth = ((PictogramElement) jptType.part().getModel())
				.getGraphicsAlgorithm().getHeight();
		
		jpaDiagramEditor.click(jptType);

		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_collapseEntityMenuItem);
		waitASecond();
		
		int newHeight = ((PictogramElement) jptType.part().getModel())
				.getGraphicsAlgorithm().getHeight();
		assertEquals("Entity must be collapsed!",
				JPAEditorConstants.ENTITY_MIN_HEIGHT, newHeight);
		assertTrue(newHeight < heigth);

		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_expandEntityMenuItem);
		waitASecond();
		
		newHeight = ((PictogramElement) jptType.part().getModel())
				.getGraphicsAlgorithm().getHeight();
		assertEquals("Entity must be expanded!", heigth, newHeight);
		assertTrue(newHeight > JPAEditorConstants.ENTITY_MIN_HEIGHT);
	}

	public void collapseExpandAllJPTsViaMenu(SWTBotGefEditPart jptType1, SWTBotGefEditPart jptType2) {

		int heigth1 = ((PictogramElement) jptType1.part().getModel())
				.getGraphicsAlgorithm().getHeight();

		int heigth2 = ((PictogramElement) jptType2.part().getModel())
				.getGraphicsAlgorithm().getHeight();

		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_collapseAllEntitiesMenuItem);
		waitASecond();
		
		// check that entity1 is collapsed
		int newHeight1 = ((PictogramElement) jptType1.part().getModel())
				.getGraphicsAlgorithm().getHeight();
		assertEquals("Entity1 must be collapsed!",
				JPAEditorConstants.ENTITY_MIN_HEIGHT, newHeight1);
		assertTrue(newHeight1 < heigth1);

		// check that entity2 is collapsed
		int newHeight2 = ((PictogramElement) jptType2.part().getModel())
				.getGraphicsAlgorithm().getHeight();
		assertEquals("Entity2 must be collapsed!",
				JPAEditorConstants.ENTITY_MIN_HEIGHT, newHeight2);
		assertTrue(newHeight2 < heigth2);

		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_expandAllEntitiesMenuItem);
		waitASecond();
		
		// check that entity1 is expanded
		newHeight1 = ((PictogramElement) jptType1.part().getModel())
				.getGraphicsAlgorithm().getHeight();
		assertEquals("Entity must be expanded!", heigth1, newHeight1);
		assertTrue(newHeight1 > JPAEditorConstants.ENTITY_MIN_HEIGHT);

		// check that entity2 is expanded
		newHeight2 = ((PictogramElement) jptType2.part().getModel())
				.getGraphicsAlgorithm().getHeight();
		assertEquals("Entity must be expanded!", heigth2, newHeight2);
		assertTrue(newHeight2 > JPAEditorConstants.ENTITY_MIN_HEIGHT);
	}

	public void discardChanges(SWTBotGefEditPart jptType, String attributeName) {

		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes,
						jptType));
		assertFalse(isJPTDirty(jptType));

		addAttributeToJPT(jptType, attributeName);
		assertTrue("Editor must be dirty", jpaDiagramEditor.isDirty());
		assertTrue(isJPTDirty(jptType));

		jptType.click();
		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_discardChangesMenuItem);
		SWTBotGefEditPart attribute = jpaDiagramEditor.getEditPart(attributeName);
		assertNull("Changes must be discard!", attribute);
		assertFalse(isJPTDirty(jptType));
	}

	public void removeAndDiscardChangesViaMenu(SWTBotGefEditPart jptType,
			String attributeName) {

		String jptName = getJPTObjectForGefElement(jptType).getSimpleName();

		assertFalse("Diagram must contain at least one entity!",
				jpaDiagramEditor.mainEditPart().children().isEmpty());

		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes, jptType));
		assertFalse(isJPTDirty(jptType));

		addAttributeToJPT(jptType, attributeName);
		assertTrue("Editor must be dirty", jpaDiagramEditor.isDirty());
		assertTrue(isJPTDirty(jptType));

		jptType.click();
		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_removeAndDiscardAllEntitiesAction);
		confirmRemoveEntitiesFromDiagramDialog();
		assertTrue("Diagram must be empty!", jpaDiagramEditor.mainEditPart()
				.children().isEmpty());

		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_showAllTheEntities);
		assertFalse("Diagram must contain at least one entity!",
				jpaDiagramEditor.mainEditPart().children().isEmpty());

		jptType = jpaDiagramEditor.getEditPart(jptName);
		SWTBotGefEditPart attribute = jpaDiagramEditor
				.getEditPart(attributeName);
		assertNull("Changes must be discard!", attribute);
		assertFalse(isJPTDirty(jptType));
	}

	public void removeAndSaveChangesViaMenu(SWTBotGefEditPart jptType, String attributeName) {

		assertFalse("Diagram must contain at least one entity!",
				jpaDiagramEditor.mainEditPart().children().isEmpty());

		String jptName = getJPTObjectForGefElement(jptType).getSimpleName();

		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes, jptType));
		assertFalse(isJPTDirty(jptType));

		addAttributeToJPT(jptType, attributeName);
		assertTrue("Editor must be dirty", jpaDiagramEditor.isDirty());
		assertTrue(isJPTDirty(jptType));

		jptType.click();
		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_removeAndSaveAllEntitiesAction);
		confirmRemoveEntitiesFromDiagramDialog();
		assertTrue("Diagram must be empty!", jpaDiagramEditor.mainEditPart()
				.children().isEmpty());

		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_showAllTheEntities);
		assertFalse("Diagram must contain at least one entity!",
				jpaDiagramEditor.mainEditPart().children().isEmpty());

		jptType = jpaDiagramEditor.getEditPart(jptName);
		SWTBotGefEditPart attribute = jpaDiagramEditor
				.getEditPart(attributeName);
		assertNotNull("Changes must be discard!", attribute);
		assertFalse(isJPTDirty(jptType));
	}

	public void saveOnlyJPT(SWTBotGefEditPart jptType, String attributeName) {

		assertFalse(
				"\"Other Attributes\" section must not be visible!",
				isSectionVisible(JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes,
						jptType));
		assertFalse(isJPTDirty(jptType));

		addAttributeToJPT(jptType, attributeName);
		assertTrue("Editor must be dirty", jpaDiagramEditor.isDirty());
		assertTrue(isJPTDirty(jptType));

		jptType.click();
		jpaDiagramEditor
				.clickContextMenu(JPAEditorMessages.JPAEditorToolBehaviorProvider_saveButtonText);
		assertTrue("Editor must be dirty", jpaDiagramEditor.isDirty());
		assertFalse(isJPTDirty(jptType));
	}

	public void testUniDirRelation(String relationFeatureName, SWTBotGefEditPart owner,
			SWTBotGefEditPart inverse, RelType reltype, String linkLabel) {

		jpaDiagramEditor.activateTool(relationFeatureName, 0);

		jpaDiagramEditor.click(owner);
		jpaDiagramEditor.click(inverse);
		bot.waitUntil(new ConnectionIsShown(owner));

		waitASecond();
		jpaDiagramEditor.activateDefaultTool();

		assertConnectionIsCreated(owner, inverse, false);

		SWTBotGefConnectionEditPart connection = owner.sourceConnections().get(
				0);
		IRelation rel = getConnection(connection);
		assertNotNull(rel);
		assertEquals(IRelation.RelDir.UNI, rel.getRelDir());
		assertEquals(reltype, rel.getRelType());

		String attributeName = testOwnerRelationAttributeProperties(rel);
		assertNull(rel.getInverseAnnotatedAttribute());

		assertAttributeIsCorretlyMapped(attributeName,
				linkLabel);

		assertUniDirRelationIsNotDeleted(owner, inverse,
				connection, attributeName);

		assertUniDirRelationIsDeleted(owner, inverse,
				connection, attributeName);
	}
	
	public void testSelfUniDirRelation(String relationFeatureName, SWTBotGefEditPart entity,
			RelType reltype, String linkLabel){
		
		jpaDiagramEditor.activateTool(relationFeatureName, 0);		
		jpaDiagramEditor.click(entity);
		jpaDiagramEditor.click(entity);
		bot.waitUntil(new ConnectionIsShown(entity));
		
		waitASecond();
		jpaDiagramEditor.activateDefaultTool();
		
		assertSelfConnectionIsCreated(entity);
		
		SWTBotGefConnectionEditPart connection = entity.sourceConnections().get(0);
		IRelation rel = getConnection(connection);
		assertNotNull(rel);
		assertEquals(IRelation.RelDir.UNI, rel.getRelDir());
		assertEquals(reltype, rel.getRelType());
		
		String attributeName = testOwnerRelationAttributeProperties(rel);
		assertNull(rel.getInverseAnnotatedAttribute());
		
		assertAttributeIsCorretlyMapped(attributeName, linkLabel);
		
		assertSelfUniDirRelationIsNotDeleted(entity, connection, attributeName);
		
		assertSelfUniDirRelationIsDeleted(entity, connection, attributeName);
	}

	public void testUniDirRelRemoveOwnerAttribute(String relationFeatureName,
			SWTBotGefEditPart owner, SWTBotGefEditPart inverse,
			RelType reltype, String linkLabel) {

		jpaDiagramEditor.activateTool(relationFeatureName, 0);
		jpaDiagramEditor.click(owner);
		jpaDiagramEditor.click(inverse);
		bot.waitUntil(new ConnectionIsShown(owner));

		waitASecond();
		jpaDiagramEditor.activateDefaultTool();

		assertConnectionIsCreated(owner, inverse, false);

		SWTBotGefConnectionEditPart connection = owner.sourceConnections().get(
				0);
		IRelation rel = getConnection(connection);
		assertNotNull(rel);
		assertEquals(IRelation.RelDir.UNI, rel.getRelDir());
		assertEquals(reltype, rel.getRelType());

		String attributeName = testOwnerRelationAttributeProperties(rel);
		assertNull(rel.getInverseAnnotatedAttribute());

		assertAttributeIsCorretlyMapped(attributeName, linkLabel);

		// delete the owner attribute
		SWTBotGefEditPart ownerAttrPart = jpaDiagramEditor
				.getEditPart(attributeName);
		ownerAttrPart.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor, attributeName),
				10000);
		// assert that the connection does not exists anymore
		assertTrue(owner.sourceConnections().isEmpty());
		assertTrue(inverse.targetConnections().isEmpty());
		assertNull(jpaDiagramEditor.getEditPart(attributeName));
		assertFalse(isSectionVisible(owner, JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	public void testBiDirRel(String relationFeatureName, SWTBotGefEditPart owner,
			SWTBotGefEditPart inverse, RelType reltype, String linkLabel) {
		testBiDirRelWithTwoMappingTypes(relationFeatureName,
				owner, inverse, reltype, linkLabel, linkLabel);
	}
	
	public void testSelfBiDirRel(String relationFeatureName, SWTBotGefEditPart owner,
			RelType reltype, String linkLabel) {
		testSelfBiDirRelWithTwoMappings(relationFeatureName,
				owner, reltype, linkLabel, linkLabel);
	}

	public void testBiDirRelWithTwoMappingTypes(String relationFeatureName,
			SWTBotGefEditPart owner, SWTBotGefEditPart inverse,
			RelType reltype, String ownerLinkLabel, String inverseLinkLabel) {

		jpaDiagramEditor.activateTool(relationFeatureName, 1);
		jpaDiagramEditor.click(owner);
		jpaDiagramEditor.click(inverse);
		bot.waitUntil(new ConnectionIsShown(owner));

		waitASecond();
		jpaDiagramEditor.activateDefaultTool();

		assertConnectionIsCreated(owner, inverse, true);

		SWTBotGefConnectionEditPart connection = owner.sourceConnections().get(
				0);
		IRelation rel = getConnection(connection);
		assertNotNull(rel);
		assertEquals(IRelation.RelDir.BI, rel.getRelDir());
		assertEquals(reltype, rel.getRelType());

		String ownerAttributeName = testOwnerRelationAttributeProperties(rel);

		String inverseAttributeName = testInverseRelationAttributeProperties(rel);

		assertAttributeIsCorretlyMapped(ownerAttributeName, ownerLinkLabel);
		assertAttributeIsCorretlyMapped(inverseAttributeName, inverseLinkLabel);

		assertBiDirRelationIsNotDeleted(owner, inverse,
				connection, ownerAttributeName, inverseAttributeName);

		assertBiDirRelationIsDeleted(owner, inverse,
				connection, ownerAttributeName, inverseAttributeName);
	}
	
	public void testSelfBiDirRelWithTwoMappings(String relationFeatureName,
			SWTBotGefEditPart entity, RelType reltype, String ownerLinkLabel, String inverseLinkLabel){
		
		jpaDiagramEditor.activateTool(relationFeatureName, 1);		
		jpaDiagramEditor.click(entity);
		jpaDiagramEditor.click(entity);		
		bot.waitUntil(new ConnectionIsShown(entity));
		
		waitASecond();
		jpaDiagramEditor.activateDefaultTool();
		
		assertSelfConnectionIsCreated(entity);
		
		SWTBotGefConnectionEditPart connection = entity.sourceConnections().get(0);
		IRelation rel = getConnection(connection);
		assertNotNull(rel);
		assertEquals(IRelation.RelDir.BI, rel.getRelDir());
		assertEquals(reltype, rel.getRelType());
		
		String ownerAttributeName = testOwnerRelationAttributeProperties(rel);
		
		String inverseAttributeName = testInverseRelationAttributeProperties(rel);
		
		assertAttributeIsCorretlyMapped(ownerAttributeName, ownerLinkLabel);
		assertAttributeIsCorretlyMapped(inverseAttributeName, inverseLinkLabel);
		
		assertSelfBiDirRelationIsNotDeleted(entity, connection, ownerAttributeName,
				inverseAttributeName);
		
		assertSelfBiDirRelationIsDeleted(entity, connection, ownerAttributeName, inverseAttributeName);
	}

	public void testBiDirRelRemoveInverseAttribute(String relationFeatureName,
			SWTBotGefEditPart owner, SWTBotGefEditPart inverse,
			RelType reltype, String linkLabel) {

		testBiDirRelWithTwoMappingsWithoutInverseAttr(relationFeatureName, owner, inverse, reltype, linkLabel,
				linkLabel);
	}

	public void testBiDirRelWithTwoMappingsWithoutInverseAttr(String relationFeatureName,
			SWTBotGefEditPart owner, SWTBotGefEditPart inverse,
			RelType reltype, String ownerLinkLabel, String inverseLinkLabel) {

		jpaDiagramEditor.activateTool(relationFeatureName, 1);
		jpaDiagramEditor.click(owner);
		jpaDiagramEditor.click(inverse);
		bot.waitUntil(new ConnectionIsShown(owner));

		waitASecond();
		jpaDiagramEditor.activateDefaultTool();

		assertConnectionIsCreated(owner, inverse, true);

		SWTBotGefConnectionEditPart connection = owner.sourceConnections().get(
				0);
		IRelation rel = getConnection(connection);
		assertNotNull(rel);
		assertEquals(IRelation.RelDir.BI, rel.getRelDir());
		assertEquals(reltype, rel.getRelType());

		String ownerAttributeName = testOwnerRelationAttributeProperties(rel);

		String inverseAttributeName = testInverseRelationAttributeProperties(rel);

		assertAttributeIsCorretlyMapped(ownerAttributeName,	ownerLinkLabel);
		assertAttributeIsCorretlyMapped(inverseAttributeName, inverseLinkLabel);

		// delete the inverse attribute
		SWTBotGefEditPart inverseAttr = jpaDiagramEditor
				.getEditPart(inverseAttributeName);
		inverseAttr.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		confirmDelete();

		waitASecond();
		// assert that the connection still exists, but it is unidirectional now
		assertConnectionIsCreated(owner, inverse, false);
		connection = owner.sourceConnections().get(0);
		rel = getConnection(connection);
		assertNotNull(rel);
		assertEquals(IRelation.RelDir.UNI, rel.getRelDir());
		assertEquals(reltype, rel.getRelType());
		assertEquals(ownerAttributeName,
				testOwnerRelationAttributeProperties(rel));
		assertNull(rel.getInverseAnnotatedAttribute());
		assertAttributeIsCorretlyMapped(ownerAttributeName,
				ownerLinkLabel);

		// delete the owner attribute
		SWTBotGefEditPart ownerAttr = jpaDiagramEditor
				.getEditPart(ownerAttributeName);
		ownerAttr.click();
		jpaDiagramEditor.clickContextMenu("Delete");
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor,
				ownerAttributeName), 10000);
		// assert that the connection does not exists anymore
		assertTrue(owner.sourceConnections().isEmpty());
		assertTrue(inverse.targetConnections().isEmpty());
		assertNull(jpaDiagramEditor.getEditPart(ownerAttributeName));
		assertNull(jpaDiagramEditor.getEditPart(inverseAttributeName));
		assertFalse(isSectionVisible(owner,
				JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
		assertFalse(isSectionVisible(inverse,
				JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	public void testBiDirRelRemoveOwnerAttr(String relationFeatureName, SWTBotGefEditPart owner,
			SWTBotGefEditPart inverse, RelType reltype, String linkLabel) {
		testBiDirRelWithTwoMappingsWithoutOwnerAttr(
				relationFeatureName, owner, inverse, reltype, linkLabel,
				linkLabel);
	}

	public void testBiDirRelWithTwoMappingsWithoutOwnerAttr(String relationFeatureName,
			SWTBotGefEditPart owner, SWTBotGefEditPart inverse,
			RelType reltype, String ownerLinkLabel, String inverseLinkLabel) {

		jpaDiagramEditor.activateTool(relationFeatureName, 1);
		jpaDiagramEditor.click(owner);
		jpaDiagramEditor.click(inverse);
		bot.waitUntil(new ConnectionIsShown(owner));

		waitASecond();
		jpaDiagramEditor.activateDefaultTool();

		assertConnectionIsCreated(owner, inverse, true);

		SWTBotGefConnectionEditPart connection = owner.sourceConnections().get(
				0);
		IRelation rel = getConnection(connection);
		assertNotNull(rel);
		assertEquals(IRelation.RelDir.BI, rel.getRelDir());
		assertEquals(reltype, rel.getRelType());

		String ownerAttributeName = testOwnerRelationAttributeProperties(rel);

		String inverseAttributeName = testInverseRelationAttributeProperties(rel);

		assertAttributeIsCorretlyMapped(ownerAttributeName,	ownerLinkLabel);
		assertAttributeIsCorretlyMapped(inverseAttributeName, inverseLinkLabel);

		// delete the owner attribute
		SWTBotGefEditPart ownerAttrPart = jpaDiagramEditor
				.getEditPart(ownerAttributeName);
		ownerAttrPart.select();
		jpaDiagramEditor.clickContextMenu("Delete");
		confirmDelete();
		bot.waitUntil(new ElementDisappears(jpaDiagramEditor,
				ownerAttributeName), 10000);
		// assert that the connection does not exists anymore
		assertTrue(owner.sourceConnections().isEmpty());
		assertTrue(inverse.targetConnections().isEmpty());
		assertNull(jpaDiagramEditor.getEditPart(ownerAttributeName));
		// assert that the inverse attribute still exists
		assertNotNull(jpaDiagramEditor.getEditPart(inverseAttributeName));
		assertFalse(isSectionVisible(owner,
				JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
		// assert that the inverse attribute still exists
		assertTrue(isSectionVisible(inverse,
				JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
	}

	public void createInheritedEntity(SWTBotGefEditPart superclass, String subclassName,
			String superclassMappingLinkLabel, boolean byMappedSuperclass) {

		String superclassName = getJPTObjectForGefElement(superclass).getSimpleName();

		jpaDiagramEditor
				.activateTool(JPAEditorMessages.CreateJPAEntityFromMappedSuperclassFeature_createInheritedEntityFeatureName);
		jpaDiagramEditor.click(superclass);
		jpaDiagramEditor.click(50, 200);

		bot.waitUntil(new ElementIsShown(jpaDiagramEditor, subclassName), 10000);

		SWTBotGefEditPart inheritedEntity = jpaDiagramEditor
				.getEditPart(subclassName);
		assertNotNull(inheritedEntity);
		if(byMappedSuperclass){
			assertTrue(isSectionVisible(inheritedEntity,
					JPAEditorMessages.AddJPAEntityFeature_primaryKeysShape));
		} else {
			assertFalse(isSectionVisible(inheritedEntity,
					JPAEditorMessages.AddJPAEntityFeature_primaryKeysShape));
		}
		assertFalse(isSectionVisible(inheritedEntity,
				JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes));
		assertFalse(isSectionVisible(inheritedEntity,
				JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes));

		jpaDiagramEditor.activateDefaultTool();

		assertIsARelationExists(superclass, inheritedEntity);

		SWTBotGefConnectionEditPart connection = superclass.targetConnections()
				.get(0);
		FreeFormConnection conn = (FreeFormConnection) connection.part()
				.getModel();
		assertTrue(IsARelation.isIsAConnection(conn));
		IsARelation rel = getIsARelationship();
		assertNotNull(rel);

		assertEquals(getJPTObjectForGefElement(inheritedEntity), rel.getSubclass());
		assertEquals(getJPTObjectForGefElement(superclass),	rel.getSuperclass());

		assertTypeIsCorretlyMapped(superclassName, superclassMappingLinkLabel);
		assertTypeIsCorretlyMapped(subclassName, JptUiDetailsMessages.EntityUiProvider_linkLabel);
	}
	
	public void testNoConnectionIsCreated(String relationFeatureName,
			int indexInPallete, SWTBotGefEditPart owner,
			SWTBotGefEditPart inverse) {
		
		jpaDiagramEditor.activateTool(relationFeatureName, indexInPallete);

		jpaDiagramEditor.click(owner);
		jpaDiagramEditor.click(inverse);

		waitASecond();

		assertTrue("There is no connection created.", owner
				.sourceConnections().isEmpty());
		assertTrue("There is no connection created.", inverse
				.sourceConnections().isEmpty());
		assertTrue("There is no connection.", inverse.targetConnections()
				.isEmpty());
	}
	
	public void testNoConnectionIsCreatedWithEmbeddable(String relationFeatureName,
			int indexInPallete, SWTBotGefEditPart owner,
			SWTBotGefEditPart inverse) {
		
		jpaDiagramEditor.activateTool(relationFeatureName, indexInPallete);

		jpaDiagramEditor.click(owner);
		jpaDiagramEditor.click(inverse);

		waitASecond();

		assertTrue("There is no connection created.", owner
				.sourceConnections().isEmpty());
		assertTrue("There is no connection created.", inverse
				.sourceConnections().isEmpty());
	}
	
	public void testNoEmbeddedConnectionIsCreated(String relationFeatureName,
			int indexInPallete, SWTBotGefEditPart owner,
			SWTBotGefEditPart inverse, boolean alreadyEmbed) {
		
		jpaDiagramEditor.activateTool(relationFeatureName, indexInPallete);

		jpaDiagramEditor.click(owner);
		jpaDiagramEditor.click(inverse);

		waitASecond();

		assertTrue("There is no connection created.", owner
				.sourceConnections().isEmpty());
		if(alreadyEmbed){
			assertFalse("There is no connection created.", inverse
					.sourceConnections().isEmpty());
		} else {
			assertTrue("There is no connection created.", inverse
					.sourceConnections().isEmpty());
		}
		assertTrue("There is no connection.", inverse.targetConnections()
				.isEmpty());
	}

	public void setJpaDiagramEditor(SWTBotGefEditor jpaDiagramEditor) {
		this.jpaDiagramEditor = jpaDiagramEditor;
	}
}
