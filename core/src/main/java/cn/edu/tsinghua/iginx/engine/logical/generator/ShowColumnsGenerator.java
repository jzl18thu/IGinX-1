/*
 * IGinX - the polystore system with high performance
 * Copyright (C) Tsinghua University
 * TSIGinX@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package cn.edu.tsinghua.iginx.engine.logical.generator;

import cn.edu.tsinghua.iginx.engine.shared.operator.Operator;
import cn.edu.tsinghua.iginx.engine.shared.operator.ShowColumns;
import cn.edu.tsinghua.iginx.engine.shared.source.GlobalSource;
import cn.edu.tsinghua.iginx.sql.statement.ShowColumnsStatement;
import cn.edu.tsinghua.iginx.sql.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowColumnsGenerator extends AbstractGenerator {
  @SuppressWarnings("unused")
  private static final Logger LOGGER = LoggerFactory.getLogger(ShowColumnsGenerator.class);

  private static final ShowColumnsGenerator instance = new ShowColumnsGenerator();

  private ShowColumnsGenerator() {
    this.type = GeneratorType.ShowColumns;
  }

  public static ShowColumnsGenerator getInstance() {
    return instance;
  }

  @Override
  protected Operator generateRoot(Statement statement) {
    ShowColumnsStatement showColumnsStatement = (ShowColumnsStatement) statement;
    return new ShowColumns(
        new GlobalSource(),
        showColumnsStatement.getPathRegexSet(),
        showColumnsStatement.getTagFilter(),
        showColumnsStatement.getLimit(),
        showColumnsStatement.getOffset());
  }
}
