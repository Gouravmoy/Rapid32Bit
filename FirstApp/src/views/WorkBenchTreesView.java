package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import dao.ColumnMetaDao;
import dao.DatabaseDao;
import dao.FileDAO;
import dao.QueryDao;
import dao.TestScenarioDao;
import dao.TestSuiteDao;
import daoImpl.AllTestSuiteDaoImpl;
import daoImpl.ColumnMetaDAOImpl;
import daoImpl.DatabaseDAOImpl;
import daoImpl.FileDAOImpl;
import daoImpl.QueryDaoImpl;
import daoImpl.TestScenarioDaoImpl;
import entity.ColumnMeta;
import entity.Database;
import entity.Files;
import entity.LookUpCols;
import entity.QueryEntity;
import entity.TestScenario;
import entity.TestSuite;
import enums.DBTypes;
import enums.FileTypes;
import enums.ProjectType;
import enums.QueryType;
import exceptions.DAOException;
import exceptions.ReadEntityException;
import exceptions.ServiceException;
import listners.MousePopupListner;
import listners.TreeSelectionListner;
import service.FileService;
import service.LookUpQueryService;
import service.QueryService;
import service.TestScenarioService;
import serviceImpl.FileServiceImpl;
import serviceImpl.LookUpQueryServiceImpl;
import serviceImpl.QueryServiceImpl;
import serviceImpl.TestScenarioServiceImpl;
import util.JTreeUtil;
import views.wizardPages.workflow.EditWizard;

public class WorkBenchTreesView {
	Composite composite;
	private static JPanel panel_1;
	private static JScrollPane mainScrollPane;
	// Trees
	private static JTree metadataTree;
	private static JTree testScenariotree;
	private static JTree testSuitetree;
	// Renderer
	static DefaultTreeCellRenderer metadataTreeRenderer;
	static DefaultTreeCellRenderer testScenariotreeRenderer;
	static DefaultTreeCellRenderer testSuitetreeRenderer;

	// Top Nodes
	static DefaultMutableTreeNode metadataTop;
	static DefaultMutableTreeNode testScenariotreeTop;
	static DefaultMutableTreeNode testSuiteTop;
	// Dao Classes
	static DatabaseDao databaseDao;
	static FileDAO fileDAO;
	static ColumnMetaDao columnMetaDao;
	static QueryDao queryEntityDao;
	static TestScenarioDao testScenarioDao;
	static TestSuiteDao testSuiteDao;
	// Service
	static LookUpQueryService lookUpQueryService;
	static TestScenarioService testScenarioService;
	static FileService fileService;
	static QueryService queryService;
	// Menu
	static JPopupMenu popup = new JPopupMenu();
	static JPopupMenu queryPopUp = new JPopupMenu();
	JMenuItem refressAll;
	JMenuItem refressAllMain;
	JMenuItem edit;
	JMenuItem editQuery;
	JMenuItem editLookUpCol;
	// Wizard
	WizardDialog wizardDialog;

	@Inject
	public static EPartService partService = null;
	@Inject
	public static EModelService modelService = null;

	@Inject
	public WorkBenchTreesView() {
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		composite = new Composite(parent, SWT.EMBEDDED);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setBounds(0, 0, 277, 465);
		assignMenuItems();
		popup.add(edit);
		popup.add(refressAllMain);
		Frame frame = SWT_AWT.new_Frame(composite);
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(12, 24, 247, 405);
		frame.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		mainScrollPane = new JScrollPane();

		initilizeTrees(frame);
		try {
			createMetaDataTree();
			createTestScenario();
			createTestSuite();
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		panel_1.add(metadataTree);
		panel_1.add(testScenariotree);
		panel_1.add(testSuitetree);

		mainScrollPane.setViewportView(panel_1);
		panel.add(mainScrollPane);

	}

	public void assignMenuItems() {
		refressAll = new JMenuItem("Refresh All");
		refressAll.setActionCommand("refresh");
		refressAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					queryAndRefresh();
				} catch (DAOException | ServiceException e) {
					e.printStackTrace();
				}
			}
		});
		refressAllMain = new JMenuItem("Refresh All");
		refressAllMain.setActionCommand("refresh");
		refressAllMain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					queryAndRefresh();
				} catch (DAOException | ServiceException e) {
					e.printStackTrace();
				}
			}
		});
		edit = new JMenuItem("Edit");
		edit.setActionCommand("edit");
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JTree currentSelectedTree = null;
				DefaultMutableTreeNode node = null;
				System.out.println("Here");
				Component selectedComponent = MousePopupListner.currentComponent;
				if (selectedComponent instanceof JTree) {
					currentSelectedTree = (JTree) selectedComponent;
					node = (DefaultMutableTreeNode) currentSelectedTree.getLastSelectedPathComponent();
				}
				if (node == null)
					return;
				openEditWizard(node);
			}

			public void openEditWizard(DefaultMutableTreeNode node) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						wizardDialog = new WizardDialog(composite.getShell(), new EditWizard(node.getUserObject()));
						wizardDialog.open();
					}
				});
			}
		});
		editQuery = new JMenuItem("Edit Query");
		editQuery.setActionCommand("editQuery");
		editQuery.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JTree currentSelectedTree = null;
				DefaultMutableTreeNode node = null;
				System.out.println("Here");
				Component selectedComponent = MousePopupListner.currentComponent;
				if (selectedComponent instanceof JTree) {
					currentSelectedTree = (JTree) selectedComponent;
					node = (DefaultMutableTreeNode) currentSelectedTree.getLastSelectedPathComponent();
				}
				if (node == null)
					return;
				openEditWizard(node);
			}

			public void openEditWizard(DefaultMutableTreeNode node) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						wizardDialog = new WizardDialog(composite.getShell(), new EditWizard(node.getUserObject()));
						wizardDialog.open();
					}
				});
			}
		});
		editLookUpCol = new JMenuItem("Edit LookUpCol Query");
		editLookUpCol.setActionCommand("editLookUpColQuery");
		editLookUpCol.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JTree currentSelectedTree = null;
				DefaultMutableTreeNode node = null;
				System.out.println("Here");
				Component selectedComponent = MousePopupListner.currentComponent;
				if (selectedComponent instanceof JTree) {
					currentSelectedTree = (JTree) selectedComponent;
					node = (DefaultMutableTreeNode) currentSelectedTree.getLastSelectedPathComponent();
				}
				if (node == null)
					return;
				openEditWizard(node);
			}

			public void openEditWizard(DefaultMutableTreeNode node) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						wizardDialog = new WizardDialog(composite.getShell(), new EditWizard(node.getUserObject()));
						wizardDialog.open();
					}
				});
			}
		});

	}

	public static void queryAndRefresh() throws DAOException, ServiceException {
		createMetaDataTree();
		createTestScenario();
		createTestSuite();
		collapseAll();
	}

	private static void collapseAll() {
		JTreeUtil.colapse(metadataTree);
		JTreeUtil.colapse(testScenariotree);
		JTreeUtil.colapse(testSuitetree);
	}

	private static void createTestScenario() throws ReadEntityException, ServiceException {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode databaseCategory = null;
		DefaultMutableTreeNode databaseLookUpCategory = null;
		DefaultMutableTreeNode queryCategory = null;
		DefaultMutableTreeNode querySubCategory = null;
		boolean isThere = false;
		testScenarioDao = new TestScenarioDaoImpl();
		fileService = new FileServiceImpl();
		queryService = new QueryServiceImpl();
		lookUpQueryService = new LookUpQueryServiceImpl();
		List<TestScenario> testScenarios = testScenarioDao.getAllTestScenarios();
		category = new DefaultMutableTreeNode("...");
		testScenariotreeTop.removeAllChildren();
		if (testScenarios.size() == 0) {
			category = new DefaultMutableTreeNode("...");
		} else {
			for (TestScenario testScenario : testScenarios) {
				category = new DefaultMutableTreeNode(testScenario);
				category.add(new DefaultMutableTreeNode(testScenario.getProjectType()));
				category.add(new DefaultMutableTreeNode(testScenario.getColumnMeta()));
				if (testScenario.getProjectType() == ProjectType.File_To_File) {
					category.add(new DefaultMutableTreeNode(testScenario.getColumnMetaTarget()));
				}
				List<Database> databases = databaseDao.getAllDatabaseinDB();
				databaseCategory = new DefaultMutableTreeNode("SOURCE");
				if (testScenario.getSource() == 0) {
					databaseCategory.add(new DefaultMutableTreeNode(
							fileService.getFileForColMeta(testScenario.getColumnMeta().getIdColumnMeta())));
				} else {
					databaseCategory
							.add(new DefaultMutableTreeNode(getDBPropforId(databases, testScenario.getSource())));
				}
				databaseLookUpCategory = new DefaultMutableTreeNode("LOOK UP DB SOURCE");

				if (testScenario.getSourceLookup() != 0) {
					databaseLookUpCategory
							.add(new DefaultMutableTreeNode(getDBPropforId(databases, testScenario.getSourceLookup())));
				} else {
					databaseLookUpCategory.add(new DefaultMutableTreeNode("..."));
				}
				databaseCategory.add(databaseLookUpCategory);
				category.add(databaseCategory);
				databaseCategory = new DefaultMutableTreeNode("TARGET");
				if (testScenario.getTarget() == 0) {
					databaseCategory.add(new DefaultMutableTreeNode(
							fileService.getFileForColMeta(testScenario.getColumnMetaTarget().getIdColumnMeta())));
				} else {
					databaseCategory
							.add(new DefaultMutableTreeNode(getDBPropforId(databases, testScenario.getTarget())));
				}
				databaseLookUpCategory = new DefaultMutableTreeNode("LOOK UP DB TARGET");
				if (testScenario.getTargetLookup() != 0) {
					databaseLookUpCategory
							.add(new DefaultMutableTreeNode(getDBPropforId(databases, testScenario.getTargetLookup())));
				} else {
					databaseLookUpCategory.add(new DefaultMutableTreeNode("..."));
				}
				databaseCategory.add(databaseLookUpCategory);
				category.add(databaseCategory);

				List<QueryEntity> queryEntities = queryService
						.getQueryByColumnId(testScenario.getColumnMeta().getIdColumnMeta());
				queryCategory = new DefaultMutableTreeNode("QUERIES");
				for (QueryType queryType : QueryType.values()) {
					isThere = false;
					querySubCategory = new DefaultMutableTreeNode(queryType);
					for (QueryEntity queryEntity : queryEntities) {
						if (queryEntity.getQueryType() == queryType) {
							isThere = true;
							DefaultMutableTreeNode queryEntityMeta = new DefaultMutableTreeNode(queryEntity);
							DefaultMutableTreeNode lookUpCatagory = new DefaultMutableTreeNode("LOOK UP COLS");
							List<LookUpCols> lookUpCols = null;
							try {
								lookUpCols = lookUpQueryService.getLookUpColsByQueryId(queryEntity.getQueryId());
							} catch (ServiceException e) {
								e.printStackTrace();
							}

							if (!lookUpCols.isEmpty()) {
								for (LookUpCols singleLookUpCol : lookUpCols) {
									DefaultMutableTreeNode subSubCategory = new DefaultMutableTreeNode(singleLookUpCol);
									lookUpCatagory.add(subSubCategory);
								}
							}
							queryEntityMeta.add(lookUpCatagory);
							querySubCategory.add(queryEntityMeta);
							// queryCategory.add(querySubCategory);
						}

					}
					if (isThere == false) {
						querySubCategory.add(new DefaultMutableTreeNode("..."));
					}
					queryCategory.add(querySubCategory);
					category.add(queryCategory);
				}
				testScenariotreeTop.add(category);
			}
		}
		refreshTestScenarioTree();
	}

	private static Database getDBPropforId(List<Database> databases, Long long1) {
		for (Database database : databases) {
			if (database.getDbId() == long1) {
				return database;
			}
		}
		return null;

	}

	private static void createTestSuite() throws ReadEntityException {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode subCatagory = null;
		DefaultMutableTreeNode subSubCatagory = null;

		boolean isThereMain = false;
		boolean isThere = false;
		testSuiteDao = new AllTestSuiteDaoImpl();
		testScenarioService = new TestScenarioServiceImpl();
		List<TestSuite> suites = testSuiteDao.getAllTestSuiteInDB();
		testSuiteTop.removeAllChildren();
		category = new DefaultMutableTreeNode();
		for (TestSuite testSuite : suites) {
			isThereMain = true;
			isThere = false;
			category = new DefaultMutableTreeNode(testSuite);
			List<TestScenario> testScenarios = testScenarioService.getScenariosForSuiteId(testSuite.getTestSuiteId());
			subCatagory = new DefaultMutableTreeNode("TEST SCENARIOS");
			for (TestScenario testScenario : testScenarios) {
				isThere = true;
				subSubCatagory = new DefaultMutableTreeNode(testScenario);
				subCatagory.add(subSubCatagory);
			}
			if (isThere == false) {
				subCatagory.add(new DefaultMutableTreeNode("..."));
			}
			category.add(subCatagory);
			testSuiteTop.add(category);
		}
		if (isThereMain == false) {
			category.add(new DefaultMutableTreeNode("..."));
		}
		testSuiteTop.add(category);
		refreshTestSuiteTree();
	}

	static void createMetaDataTree() throws DAOException {
		DefaultMutableTreeNode superCategory = null;
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode database = null;
		DefaultMutableTreeNode files = null;
		DefaultMutableTreeNode columMeta = null;

		boolean isThere = false;

		databaseDao = new DatabaseDAOImpl();
		List<Database> databases = databaseDao.getAllDatabaseinDB();

		metadataTop.removeAllChildren();
		superCategory = new DefaultMutableTreeNode("DATABASES");
		for (DBTypes dbType : DBTypes.values()) {
			isThere = false;
			category = new DefaultMutableTreeNode(dbType);
			for (Database singleDatabase : databases) {
				if (singleDatabase.getDbType() == dbType) {
					isThere = true;
					database = new DefaultMutableTreeNode(singleDatabase);
					category.add(database);
				}
			}
			if (isThere == false) {
				category.add(new DefaultMutableTreeNode("..."));
			}
			superCategory.add(category);
			metadataTop.add(superCategory);
		}
		// Files Tree
		isThere = false;
		superCategory = new DefaultMutableTreeNode("FILES");
		fileDAO = new FileDAOImpl();
		List<Files> allfiles = fileDAO.getAllFiles();
		for (FileTypes fileType : FileTypes.values()) {
			isThere = false;
			category = new DefaultMutableTreeNode(fileType);
			for (Files singleFile : allfiles) {
				if (singleFile.getFileTypes() == fileType) {
					isThere = true;
					files = new DefaultMutableTreeNode(singleFile);
					category.add(files);
				}
			}
			if (isThere == false) {
				category.add(new DefaultMutableTreeNode("..."));
			}

			superCategory.add(category);
			metadataTop.add(superCategory);
		}
		// ColumnMeta
		isThere = false;
		superCategory = new DefaultMutableTreeNode("COLUMN METEDATA");
		columnMetaDao = new ColumnMetaDAOImpl();
		List<ColumnMeta> columnMetas = columnMetaDao.getAllColumnMetas();

		for (ColumnMeta columnMeta : columnMetas) {
			isThere = true;
			columMeta = new DefaultMutableTreeNode(columnMeta);
			superCategory.add(columMeta);
		}
		if (isThere == false) {
			superCategory.add(new DefaultMutableTreeNode("..."));
		}
		metadataTop.add(superCategory);
		// Queries
		isThere = false;
		createQueryEntity();
		refreshMetadataTree();
	}

	public static void createQueryEntity() throws ReadEntityException {
		DefaultMutableTreeNode superCategory = null;
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode subCategory = null;
		DefaultMutableTreeNode subSubCategory = null;
		DefaultMutableTreeNode queryEntityMeta = null;
		boolean isThere = false;
		List<LookUpCols> lookUpCols;
		lookUpCols = new ArrayList<>();
		superCategory = new DefaultMutableTreeNode("QUERIES");
		lookUpQueryService = new LookUpQueryServiceImpl();
		queryEntityDao = new QueryDaoImpl();
		List<QueryEntity> queryEntities = queryEntityDao.getAllQueries();

		for (QueryType queryType : QueryType.values()) {
			isThere = false;
			category = new DefaultMutableTreeNode(queryType);
			for (QueryEntity queryEntity : queryEntities) {
				if (queryEntity.getQueryType() == queryType) {
					isThere = true;
					queryEntityMeta = new DefaultMutableTreeNode(queryEntity);
					subCategory = new DefaultMutableTreeNode("LOOK UP COLS");
					try {
						lookUpCols = lookUpQueryService.getLookUpColsByQueryId(queryEntity.getQueryId());
					} catch (ServiceException e) {
						e.printStackTrace();
					}

					if (!lookUpCols.isEmpty()) {
						for (LookUpCols singleLookUpCol : lookUpCols) {
							subSubCategory = new DefaultMutableTreeNode(singleLookUpCol);
							subCategory.add(subSubCategory);
						}
					}
					queryEntityMeta.add(subCategory);
					category.add(queryEntityMeta);
				}
			}
			if (isThere == false) {
				category.add(new DefaultMutableTreeNode("..."));
			}
			superCategory.add(category);
			metadataTop.add(superCategory);
		}
	}

	private static void refreshMetadataTree() {
		DefaultTreeModel model = (DefaultTreeModel) metadataTree.getModel();
		model.reload(metadataTop);
		JTreeUtil.colapse(metadataTree);
	}

	private static void refreshTestScenarioTree() {
		DefaultTreeModel model = (DefaultTreeModel) testScenariotree.getModel();
		model.reload(testScenariotreeTop);
		JTreeUtil.colapse(testScenariotree);
	}

	private static void refreshTestSuiteTree() {
		DefaultTreeModel model = (DefaultTreeModel) testSuitetree.getModel();
		model.reload(testSuiteTop);
		JTreeUtil.colapse(testSuitetree);
	}

	private void initilizeTrees(Frame frame) {
		ImageIcon metadataImageIcon = new ImageIcon(WorkBenchTreesView.class.getResource("/icons/transform_flip.png"));
		metadataTreeRenderer = new DefaultTreeCellRenderer();
		metadataTreeRenderer.setIcon(new ImageIcon(WorkBenchTreesView.class.getResource("/icons/transform_flip.png")));
		metadataTreeRenderer.setLeafIcon(metadataImageIcon);
		metadataTop = new DefaultMutableTreeNode("METADATA");
		metadataTree = new JTree(metadataTop);
		metadataTree.setAlignmentX(Component.LEFT_ALIGNMENT);
		metadataTree.setAlignmentY(Component.TOP_ALIGNMENT);
		metadataTree.setBounds(322, 252, 104, 16);
		metadataTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		metadataTree.setShowsRootHandles(true);
		metadataTree.addTreeSelectionListener(new TreeSelectionListner(metadataTree));
		metadataTree.setCellRenderer(metadataTreeRenderer);
		metadataTree.addMouseListener(new MousePopupListner(popup, panel_1));
		ImageIcon testScenarioImageIcon = new ImageIcon(WorkBenchTreesView.class.getResource("/icons/test.png"));
		testScenariotreeRenderer = new DefaultTreeCellRenderer();
		testScenariotreeRenderer.setIcon(new ImageIcon(WorkBenchTreesView.class.getResource("/icons/test.png")));
		testScenariotreeRenderer.setLeafIcon(testScenarioImageIcon);
		testScenariotreeTop = new DefaultMutableTreeNode("TEST SCENARIOS");
		testScenariotree = new JTree(testScenariotreeTop);
		testScenariotree.setAlignmentX(Component.LEFT_ALIGNMENT);
		testScenariotree.setAlignmentY(Component.TOP_ALIGNMENT);
		testScenariotree.setBounds(322, 252, 104, 16);
		testScenariotree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		testScenariotree.setShowsRootHandles(true);
		testScenariotree.addTreeSelectionListener(new TreeSelectionListner(testScenariotree));
		testScenariotree.setCellRenderer(testScenariotreeRenderer);
		testScenariotree.addMouseListener(new MousePopupListner(popup, panel_1));
		ImageIcon testSuiteImageIcon = new ImageIcon(WorkBenchTreesView.class.getResource("/icons/lightning.png"));
		testSuitetreeRenderer = new DefaultTreeCellRenderer();
		testSuitetreeRenderer.setIcon(new ImageIcon(WorkBenchTreesView.class.getResource("/icons/lightning.png")));
		testSuitetreeRenderer.setLeafIcon(testSuiteImageIcon);
		testSuiteTop = new DefaultMutableTreeNode("TEST SUITES");
		testSuitetree = new JTree(testSuiteTop);
		testSuitetree.setAlignmentX(Component.LEFT_ALIGNMENT);
		testSuitetree.setAlignmentY(Component.TOP_ALIGNMENT);
		testSuitetree.setBounds(322, 252, 104, 16);
		testSuitetree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		testSuitetree.setShowsRootHandles(true);
		testSuitetree.addTreeSelectionListener(new TreeSelectionListner(metadataTree));
		testSuitetree.setCellRenderer(testSuitetreeRenderer);
		testSuitetree.addMouseListener(new MousePopupListner(popup, panel_1));

	}

}